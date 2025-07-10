def serialize_tag(tag_doc):
    tag_doc["_id"] = str(tag_doc["_id"])
    return tag_doc
