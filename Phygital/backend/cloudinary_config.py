import cloudinary
import os
import dotenv

dotenv.load_dotenv()

cloudinary.config(
    cloud_name=os.getenv('CLOUDINARY_NAME'),
    api_key=os.getenv('CLOUDINARY_API_KEY'),
    api_secret=os.getenv('CLOUDINARY_API_SECRET'),
)

# if __name__ == "__main__":
#     # Test the Cloudinary configuration
#     print("Cloudinary configuration:")
#     print(f"Cloud Name: {cloudinary.config().cloud_name}")
#     print(f"API Key: {cloudinary.config().api_key}")
#     print(f"API Secret: {cloudinary.config().api_secret}")