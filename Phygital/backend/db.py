from pymongo import MongoClient
import dotenv
import os

dotenv.load_dotenv()

MONGO_URI = os.getenv('MONGO_URI')
DB_NAME = os.getenv("MONGO_DB")

client = MongoClient(MONGO_URI)
db = client[DB_NAME]
tags_collection = db["tags"]
