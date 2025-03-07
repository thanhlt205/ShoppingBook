const mongoose = require('mongoose');
const Scheme = mongoose.Schema;

const Product = new Scheme({
    images: { type: Array },
    name: { type: String},
    price: { type: Number},
    description: { type: String },
    status: { type: String },
},{
    timestamps: true
})

module.exports = mongoose.model('product', Product)