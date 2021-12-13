package com.example.hyperxpress.service.constants

class HyperXpressConstants private constructor(){

    object HTTP {
        const val SUCCESS = 200
        const val CREATED = 201
        const val NOTFOUND = 404
        const val NOCONTENT = 204
    }
    object HEADER{
        const val APPLICATIONJSON = "Content-Type: application/json"
        const val MULTPARTFORMDATA = "Content-Type: multipart/form-data"
    }
    object URLS{
        private const val URLLOCALPORTADEFAUL = "http://192.168.18.10/"
        private const val URLNUVEM = "http://52.70.185.19/"
        private const val URLNUVEMCONTAINER = "http://34.226.32.237/"
        const val HYPERXPRESS = URLNUVEMCONTAINER
        const val VIACEP = "http://viacep.com.br/ws/"
    }
}