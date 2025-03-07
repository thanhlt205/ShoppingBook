const mongoose = require('mongoose');
const Scheme = mongoose.Schema;

const StatusProduct = new Scheme({
    idProduct: { type: String},
    priceProduct: { type: String},
    status: { type: String},
},{
    timestamps: true
})

module.exports = mongoose.model('statusProduct', StatusProduct)