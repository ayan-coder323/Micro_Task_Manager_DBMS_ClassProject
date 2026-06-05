from fastapi import APIRouter,Header
from Models.schemas import TaskSchema
import httpx


router = APIRouter(prefix="/taskservice")

SPRING_URL = "http://localhost:8002/"



@router.post("/createtask")
async def signup(T: TaskSchema,token:str = Header(...)):
    async with httpx.AsyncClient() as client:
        response = await client.post(
            SPRING_URL + "task/createtask",
            json=T.model_dump()
        )
    return response.json()