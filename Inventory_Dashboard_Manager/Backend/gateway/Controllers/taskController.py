from fastapi import APIRouter, Header
from Models.schemas import TaskSchema, TaskUpdateSchema
import httpx


router = APIRouter(prefix="/api/tasks")

NODE_URL = "http://localhost:8002/"


@router.post("/createtask")
async def createTask(T: TaskSchema, Authorization: str = Header(...)):
    token = Authorization.replace("Bearer ", "") if Authorization.startswith("Bearer ") else Authorization
    async with httpx.AsyncClient() as client:
        response = await client.post(
            NODE_URL + "task/createtask",
            headers={"Token": token},
            json=T.model_dump()
        )
    return response.json()

@router.get("/getalltasks/{PAGE}/{SIZE}")
async def getAllTasks(PAGE: int, SIZE: int, Authorization: str = Header(...)):
    token = Authorization.replace("Bearer ", "") if Authorization.startswith("Bearer ") else Authorization
    async with httpx.AsyncClient() as client:
        response = await client.get(
            NODE_URL + f"task/getalltasks/{PAGE}/{SIZE}",
            headers={"Token": token}
        )
    return response.json()

@router.delete("/deletetask/{ID}")
async def deleteTask(ID: str, Authorization: str = Header(...)):
    token = Authorization.replace("Bearer ", "") if Authorization.startswith("Bearer ") else Authorization
    async with httpx.AsyncClient() as client:
        response = await client.delete(
            NODE_URL + f"task/deletetask/{ID}",
            headers={"Token": token}
        )
    return response.json()

@router.put("/updatetask/{ID}")
async def updateTask(ID: str, T: TaskUpdateSchema, Authorization: str = Header(...)):
    token = Authorization.replace("Bearer ", "") if Authorization.startswith("Bearer ") else Authorization
    async with httpx.AsyncClient() as client:
        response = await client.put(
            NODE_URL + f"task/updatetask/{ID}",
            headers={"Token": token},
            json=T.model_dump(exclude_none=True)
        )
    return response.json()
