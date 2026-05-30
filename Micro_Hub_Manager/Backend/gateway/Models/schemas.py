from pydantic import BaseModel

class SignupSchema(BaseModel): # Predefined class for the sign up request body
    fullname: str
    email: str
    phone: str
    password: str

class SigninSchema(BaseModel): # Predefined class for the sign in request body
    username: str # Dont get confused if you see username parameter here it is for the email actually it is being managed at SpringBoot
    password: str

class UserSchema(BaseModel):
    fullname: str
    email: str
    phone: str
    role: int
    status: int 
    password: str



