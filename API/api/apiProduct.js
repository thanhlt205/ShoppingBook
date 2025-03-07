const express = require("express");
const routes = express.Router();
const Product = require("../models/product");
const upload = require("../config/upload");

// Them san pham
routes.post("/addProduct", upload.array('images', 5), async (req, res) => {
    try {
        // Upload image
        const { files } = req
        const urlImage = files.map((file) => `${req.get('host')}/uploads/${file.filename}`)
        // Tao doi tuong
        const model = new Product(req.body);
        // Thay doi duong dan image
        model.images = urlImage;
        // luu doi tuong vao database
        const resuls = await model.save();

        if (resuls) {
            res.json({
                status: 200,
                message: "Them san pham thanh cong",
                data: resuls,
            })
        } else {
            res.json({
                status: 400,
                message: "Them san pham khong thanh cong",
                data: "error",
            })
        }

    } catch (error) {
        res.status(400).json({ messaga: error.message });
    }
})

// Cap nhat san pham
routes.put("/updateProduct/:id", upload.array('images', 5), async (req, res) => {
    try {
        // Upload image
        const { files } = req
        const urlImage = files.map((file) => `${req.get('host')}/uploads/${file.filename}`)
        // Cap nhat doi tuong
        const product = await Product.findByIdAndUpdate(req.params.id, req.body);
        // Thay doi duong dan image
        product.images = urlImage;
        // Luu doi tuong vao database
        const resuls = await product.save();
        if (resuls) {
            res.json({
                status: 200,
                message: "Cap nhat san pham thanh cong",
                data: resuls,
            })
        } else {
            res.json({
                status: 400,
                message: "Cap nhat san pham khong thanh cong",
                data: "error",
            })
        }

    } catch (error) {
        res.status(400).json({ messaga: error.message });
    }
})

// Xoa san pham
routes.delete("/deleteProduct/:id", async (req, res) => {
    try {
        const resuls = await Product.findByIdAndDelete(req.params.id);
        if (resuls) {
            res.json({
                status: 200,
                message: "Xoa san pham thanh cong",
                data: resuls,
            })
        } else {
            res.json({
                status: 400,
                message: "Xoa san pham khong thanh cong",
                data: "error",
            })
        }
    } catch (error) {
        res.status(400).json({ messaga: error.message });
    }
})

// Lay danh sach san pham
routes.get("/listProduct", async (req, res) => {
    try {
        const resuls = await Product.find();
        if (resuls) {
            res.json({
                status: 200,
                message: "lay danh sach thanh cong",
                data: resuls,
            })
        } else {
            res.json({
                status: 400,
                message: "lay danh sach khong thanh cong",
                data: "error",
            })
        }
    } catch (error) {
        res.status(400).json({ messaga: error.message });
    }
})

// Lay san pham theo id
routes.get("/getProduct/:id", async (req, res) => {
    try {
        const resuls = await Product.findById(req.params.id);
        if (resuls) {
            res.json({
                status: 200,
                message: "lay san pham thanh cong",
                data: resuls,
            })
        } else {
            res.json({
                status: 400,
                message: "lay san pham khong thanh cong",
                data: "error",
            })
        }
    } catch (error) {
        res.status(400).json({ messaga: error.message });
    }
})

routes.get("/search", async (req, res) => {
    try {
        const key = req.query.key;
        const resuls = await Product.find({name:{"$regex":key,"$options":"i"}})
                                    .sort({createAt:-1})
        if (resuls) {
            res.json({
                status: 200,
                message: "Tim danh sach thanh cong",
                data: resuls,
            })
        } else {
            res.json({
                status: 400,
                message: "Tim danh sach khong thanh cong",
                data: "error",
            })
        }
    } catch (error) {
        res.status(400).json({ messaga: error.message });
    }
})

module.exports = routes;