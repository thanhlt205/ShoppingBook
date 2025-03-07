const express = require("express");
const router = express.Router();
const ProducFivorite = require("../models/producFivorite");

router.post("/addProducFivorite", async(req, res) =>{
    // Tao doi tuong
    const model = new ProducFivorite(req.body);
    // Luu doi tuong vao database
    const resuls = await model.save();
    if(resuls){
        res.json({
            status: 200,
            message: "Them thanh cong",
            data: resuls,
        })
    }else{
        res.json({
            status: 400,
            message: "Them that bai",
            data: "error"
        })
    }
})

router.get("/getAllProducFivorite", async(req, res) => {
    try {
        const resuls = await ProducFivorite.find();
        if(resuls){
            res.json({
                status: 200,
                message: "Lay du lieu thanh cong",
                data: resuls,
            })
        }else{
            res.json({
                status: 400,
                message: "Lay du lieu that bai",
                data: "error"
            })
        }
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
})

router.delete("/deleteProductFivorite/:id", async(req, res) =>{
    try {
        const resuls = await ProducFivorite.findByIdAndDelete(req.body.id);
        if(resuls){
            res.json({
                status: 200,
                message: "Xoa thanh cong",
                data: resuls,
            })
        }else{
            res.json({
                status: 400,
                message: "Xoa that bai",
                data: "error"
            })
        }
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
})
module.exports = router;