const express = require("express");
const router = express.Router();
const StatusProduct = require("../models/statusProduct");

// Them trang thai san pham
router.post("/addStatusProduct", async (req, res) => {
    try {
        // Tao doi tuong
        const model = new StatusProduct(req.body);
        // Luu doi tuong vao database
        const resuls = await model.save();
        if (resuls) {
            res.json({
                status: 200,
                message: "Them trang thai san pham thanh cong",
                data: resuls,
            });
        } else {
            res.json({
                status: 400,
                message: "Them trang thai san pham khong thanh cong",
                data: "error",
            });
        }
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
})

// Lay danh sach trang thai san pham

router.get("/listStatusProduct", async (req, res) => {
    try {
        const resuls = await StatusProduct.find();
        if (resuls) {
            res.json({
                status: 200,
                message: "Lay danh sach trang thai san pham thanh cong",
                data: resuls,
            });
        } else {
            res.json({
                status: 400,
                message: "Lay danh sach trang thai san pham khong thanh cong",
                data: "error",
            });
        }
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
})

router.get("/getStatusProducts/:status", async (req, res) => {
    try {
        const { status } = req.params; // Lấy giá trị status từ URL
        const results = await StatusProduct.find({ status: status });
        if (results && results.length > 0) {
            res.json({
                status: 200,
                message: "Lấy thông tin trạng thái sản phẩm thành công",
                data: results,
            });
        } else {
            res.json({
                status: 404,
                message: "Không tìm thấy sản phẩm với trạng thái này",
                data: [],
            });
        }
    } catch (error) {
        res.status(500).json({
            status: 500,
            message: "Lỗi hệ thống",
            error: error.message,
        });
    }
});

router.put("/updateStatusProduct/:id", async (req, res) => {
    try {
        const statusProduct = await StatusProduct.findByIdAndUpdate(req.params.id, req.body)
        if (statusProduct) {
            res.json({
                status: 200,
                message: "Cập nhật trạng thái sản phẩm thành công",
                data: statusProduct,
            });
        } else {
            res.json({
                status: 404,
                message: "Không tìm thấy trạng thái sản phẩm",
                data: "error",
            });
        }
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

module.exports = router;