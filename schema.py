DB_HOST = 'localhost'
DB_USER = 'root'
DB_PASS = '123456'
DB_NAME = 'email_classify'
DB_URL = f"mysql+pymysql://{DB_USER}:{DB_PASS}@{DB_HOST}/{DB_NAME}"
call_stack = []

def ask_first(func):
    def wrapper(*args, **kwargs):
        if input(f"\n Call \"{'.'.join(call_stack)}.{func.__name__}\"? (0 or 1)") == '1':
            func(*args, **kwargs)
        else:
            print(f"Skipped.\n")
    return wrapper

def state_logging(func):
    def wrapper(*args, **kwargs):
        global call_stack
        print('--- ' * len(call_stack) + f'>>> CALLED "{func.__name__}".')
        call_stack.append(func.__name__)
        func(*args, *kwargs)
        call_stack.pop()
        print('--- ' * len(call_stack) + f'<<< "{func.__name__}" FINISHED.')
    wrapper.__name__ = func.__name__
    return wrapper

import pandas as pd
from random import choice, randint
import pip

try:
    import pymysql
except ImportError or ModuleNotFoundError:
    pip.main(['install', 'pymysql'])

try:
    from sqlalchemy import create_engine
except ImportError or ModuleNotFoundError:
    pip.main(['install', 'sqlalchemy'])
finally:
    if 'create_engine' not in dir():
        from sqlalchemy import create_engine

if pd.__version__ < '2.0.3':
    del pd
    pip.main(['install', '--upgrade', 'pandas'])
    import pandas as pd

@state_logging
def init():
    global CLEANED_DATA, DATA_ROW_COUNT, ENGINE
    CLEANED_DATA = read_csv('cleaned_data.csv')
    DATA_ROW_COUNT = len(CLEANED_DATA)
    ENGINE = create_engine(DB_URL)

def read_csv(name):
    print(f'Reading data file "{name}"...')
    return pd.read_csv(name)

@ask_first
@state_logging
def write_to_db(data, tbl_name):
    if isinstance(data, str):
        data = read_csv(data)
    data.to_sql(tbl_name, con=ENGINE, if_exists='append', index=False)

@state_logging
def create_users():
    df = pd.DataFrame({
        'id':[1, 2],
        'email':['user1@mail.com', 'user2@mail.com'],
        'username':['1', '2'],
        'password':['1', '1']
    })
    write_to_db(df, 'user')

@state_logging
def create_labels():
    global label_cache
    label_cache = {lb:i+1 for i, lb in enumerate(CLEANED_DATA['X-Folder'].unique())}
    df = pd.DataFrame(label_cache.items(), columns=['name', 'id'])
    write_to_db(df, 'label')

@state_logging
def create_emails():
    def random_time():
        # Mon, 14 May 2001 16:39:00 -0700 (PDT)
        return (
            choice(['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']) +
            ', ' +
            str(randint(1, 31)) +
            ' ' +
            str(randint(1, 12)) + 
            ' 2001 ' +
            f'{randint(0, 23)}:{randint(0, 59)}:{randint(0, 59)}' +
            ' -0700 (PDT)'
        )
    def random_email():
        return 'user' + str(randint(1, 2)) + '@mail.com'

    def choose_receiver(label):
        receiver = randint(0, 1)
        label_of_users[receiver].add(label_cache[label])
        return receiver + 1

    df = CLEANED_DATA.copy()
    print("Creating data for fields...")

    global label_of_users
    label_of_users = [set() for _ in range(2)]

    df['id'] = list(range(1, DATA_ROW_COUNT + 1))
    df['sender'] = [random_email() for i in range(DATA_ROW_COUNT)]
    df['sent_time'] = [random_time() for i in range(DATA_ROW_COUNT)]
    df['label_id'] = df['X-Folder'].map(label_cache)
    df['receiver_id'] = df['X-Folder'].map(choose_receiver)

    df = df.drop(columns=['X-Folder'])
    df.rename(columns={'body':'content'}, inplace=True)

    global email_df
    email_df = df
    write_to_db(df, 'email')

@state_logging
def create_label_of_users():
    global label_of_users
    df = pd.DataFrame(columns=['id', 'user_id', 'label_id'])
    for i, lou in enumerate(label_of_users):
        df_tmp = pd.DataFrame()
        df_tmp['label_id'] = list(lou)
        df_tmp['user_id'] = [i + 1] * len(lou)
        df_tmp['id'] = list(range(len(df) + 1, len(df) + 1 + len(lou)))
        df = pd.concat([df, df_tmp], axis=0)

    write_to_db(df, 'label_of_user')

@state_logging
def create_samples():
    valid_labels = CLEANED_DATA['X-Folder'].value_counts()
    valid_labels = valid_labels[valid_labels >= 600]
    valid_label_ids = list(map(lambda x:label_cache[x], valid_labels.keys()))
    global email_df
    email_df = email_df[email_df['label_id'].isin(valid_label_ids)]
    df = pd.DataFrame()
    df['email_id'] = email_df['id']
    df['id'] = list(range(1, len(df) + 1))
    df['is_trained'] = [True] * len(df)
    write_to_db(df, 'sample')

def main():
    init()
    create_users()
    create_labels()
    create_emails()
    create_label_of_users()
    create_samples()

main()