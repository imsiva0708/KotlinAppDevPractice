from fastapi import FastAPI,HTTPException,UploadFile,File
import cloudinary.uploader
import cloudinary_config
import datetime
import os
from db import tags_collection
# from bson import ObjectId
from classes import TagCreate
from utils import serialize_tag


app = FastAPI()

@app.get("/")
def get_root():
    return {"Hello":"Hello"}

@app.post("/tags")
def create_tag(tag: TagCreate):
    existing = tags_collection.find_one({"tag": tag.tag})
    if existing:
        raise HTTPException(status_code=400, detail="Tag Already Exists")

    new_tag = {
        "author": tag.author,
        "tag": tag.tag,
        "created_at": datetime.datetime.now()
    }

    if tag.notes:
        new_tag["notes"] = tag.notes
    if tag.links:
        new_tag["links"] = [{"url": str(link.url), "description": link.description} for link in tag.links]
    if tag.images:
        new_tag["images"] = [{"url": str(img.url), "description": img.description, "public_id": img.public_id} for img in tag.images]

    tags_collection.insert_one(new_tag)
    return {"message": "Tag created successfully"}

@app.get("/tags")
def get_by_query(tag: str):
    existing = tags_collection.find_one({'tag': tag})
    if not existing:
        raise HTTPException(status_code=400, detail="Tag Doesn’t Exist")
    return serialize_tag(existing)

@app.get("/all_tags")
def get_all_tags():
    tags = tags_collection.find()
    return [serialize_tag(tag) for tag in tags]

@app.post("/upload_image")
async def upload_image(file: UploadFile = File(...)):
    if not file.filename.lower().endswith(('.png', '.jpg', '.jpeg', '.gif')):
        raise HTTPException(status_code=400, detail="Invalid file type. Only images are allowed.")

    try:
        result = cloudinary.uploader.upload(file.file,folder="tags_images")
        return {"url": result['secure_url'],"public_id": result['public_id']}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
@app.delete("/delete_image")
def delete_image(public_id: str):
    try:
        print(f"Deleting image with public_id: {public_id}")
        cloudinary.uploader.destroy(public_id)
        return {"message": "Image deleted successfully"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    os.system('fastapi dev main.py')    
