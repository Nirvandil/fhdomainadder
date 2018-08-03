import axios from 'axios'
const env = require('../../config/common')
export const AXIOS = axios.create({
    baseURL: `${env.path}/api`
})
