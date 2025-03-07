const mongoose = require('mongoose');
const Scheme = mongoose.Schema;

const ProductFivorite = new Scheme({
    idProduct: { type: String},
},{
    timestamps: true
})

module.exports = mongoose.model('productFivorite', ProductFivorite)