import express from "express";
import * as taskService from "../service/taskService.js";
const router = express.Router();

router.post("/createtask", async (req, res) => {
    res.json(await taskService.createTask(req.body, req.headers["token"]));
});

router.get("/getalltasks/:PAGE/:SIZE", async (req, res) => {
    const {PAGE, SIZE} = req.params;
    const response = await taskService.getAllTasks(PAGE, SIZE, req.headers.token);
    res.json(response);
});

export default router;