from typing import List, Optional
from pydantic import BaseModel, HttpUrl

class LinkItem(BaseModel):
    url: HttpUrl
    description: Optional[str] = None

class ImageItem(BaseModel):
    url: HttpUrl
    description: Optional[str] = None
    public_id: Optional[str] = None

class TagCreate(BaseModel):
    author: str
    tag: str
    notes: Optional[str] = None
    links: Optional[List[LinkItem]] = []
    images: Optional[List[ImageItem]] = []
