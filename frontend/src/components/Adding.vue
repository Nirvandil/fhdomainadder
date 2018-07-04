<template>
    <md-app>
        <md-app-content>
            <nav-tabs active="adding"></nav-tabs>
                    <md-card>
                        <form>
                            <div>
                                <md-checkbox v-model="toCf"><strong>В CloudFlare</strong></md-checkbox>
                            </div>
                            <template v-if="toCf">
                                <div>
                                    <md-checkbox v-model="jumpStart">Автоматически получить данные DNS</md-checkbox>
                                    <md-tooltip>Попытается получить все DNS-записи с текущих серверов имён, в противном
                                        случае
                                        будут добавлены только сам домен и поддомен www с A-записью, указывающей на IP
                                        из
                                        поля
                                        ниже.
                                    </md-tooltip>
                                </div>
                                <md-field>
                                    <md-input v-model.trim="apiKey" required></md-input>
                                    <label>Global API key</label>
                                </md-field>
                                <md-field>
                                    <md-input type="email" v-model.trim="email" required></md-input>
                                    <label>Email пользователя CloudFlare</label>
                                </md-field>
                                <md-field v-if="!jumpStart">
                                    <md-input v-model.trim="ip" required :disabled='toPanel' :value='ip'></md-input>
                                    <label>IP-адрес для добавляемых в CloudFlare доменов</label>
                                    <md-tooltip v-if="toPanel">В случае добавления одновременно в панель управления и в
                                        CloudFlare
                                        будет использован IP-адрес сервера
                                    </md-tooltip>
                                </md-field>
                            </template>
                            <div>
                                <md-checkbox v-model="toPanel"><strong>В панель управления</strong></md-checkbox>
                            </div>
                            <template v-if="toPanel">
                                <md-field>
                                    <md-input v-model.trim="ip" v-if="toPanel || !jumpStart" required></md-input>
                                    <label>IP сервера</label>
                                </md-field>
                                <md-field>
                                    <md-input v-model.trim="port" placeholder="Порт SSH" required></md-input>
                                    <label>3333 по умолчанию</label>
                                </md-field>
                                <md-field>
                                    <md-input type="password" v-model="password" required></md-input>
                                    <label>Пароль root на сервере</label>
                                </md-field>
                                <div>
                                    <md-switch v-model="cgi">
                                        <small>{{cgi ? 'РНР как CGI' : 'PHP как модуль Apache' }}</small>
                                    </md-switch>
                                    <md-tooltip>Режим работы РНР - как модуль Apache или как CGI</md-tooltip>
                                </div>
                            </template>
                            <template v-if="toPanel || toCf">
                                <md-field>
                                    <md-textarea required v-model.trim="domains"></md-textarea>
                                    <label>Список доменов, по одному в строке</label>
                                    <md-icon>description</md-icon>
                                </md-field>
                                <div v-if="progressCf">
                                    <md-progress-bar :md-indeterminate='false'
                                                     :md-progress-bar="progressCf"></md-progress-bar>
                                    <md-tooltip md-delay="1000">Прогресс добавления в CloudFlare</md-tooltip>
                                </div>
                                <md-divider v-if="progressCf || progressPanel"></md-divider>
                                <div v-if="progressPanel">
                                    <md-progress-bar class="md-accent" :md-indeterminate="false"
                                                     :md-progress-bar="progressPanel"></md-progress-bar>
                                    <md-tooltip md-delay="1000">Прогресс добавления в панель управления</md-tooltip>
                                </div>
                                <md-button class="md-raised md-accent" @click="sendRequest" :disabled="buttonDisabled">
                                    Добавить
                                </md-button>
                                <md-button class="md-primary md-raised right" @click="saveOutput">Сохранить вывод
                                </md-button>
                            </template>
                        </form>
                        <md-card class="output" v-if="output.length">
                            <ul style="text-align: left">
                                <li v-for="out in output" :key="out" v-html="out">
                                </li>
                            </ul>
                        </md-card>
                        <br>
                    </md-card>
                    <md-dialog :md-active.sync="userChooseOpen">
                        <md-dialog-title :md-click-outside-to-close='false'>Выберите пользователя</md-dialog-title>
                        <md-field>
                            <md-select v-model="targetUser" class="output" md-dense>
                                <md-option v-for="user in users" :key="user" :value="user">{{user}}</md-option>
                            </md-select>
                        </md-field>
                        <md-button @click="sendAddDomains">Ок</md-button>
                        <md-button @click="userChooseOpen=false">Отмена</md-button>
                    </md-dialog>
                    <md-dialog-alert :md-active.sync="alertOpen" :md-content="alertContent" md-confirm-text="OK"/>
        </md-app-content>
    </md-app>
</template>

<script>
    import {AXIOS} from './http-common'
    import NavTabs from './NavTabs'

    export default {
        name: 'DomainAdding',
        components: {NavTabs},
        data() {
            return {
                toCf: false,
                toPanel: false,
                apiKey: '',
                email: '',
                jumpStart: true,
                ip: '',
                port: 3333,
                password: '',
                domains: '',
                users: [],
                targetUser: '',
                output: [],
                buttonDisabled: false,
                alertContent: ' ',
                cgi: true,
                progressCf: 0,
                progressPanel: 0,
                alertOpen: false,
                userChooseOpen: false
            }
        },
        methods: {
            sendAddDomains: function() {
                this.userChooseOpen = false
                const getAddingRequest = () => ({
                    connectionDetails: {
                        ip: this.ip,
                        password: this.password,
                        port: this.port
                    },
                    userName: this.targetUser,
                    cgi: this.cgi
                })
                const addDomains = (domains, addingRequest) => {
                    domains.map(async domain => {
                        addingRequest.domain = domain
                        const result = await AXIOS.post('/panel/add', addingRequest)
                        const source = result.data
                        const message = source ? `<strong>PANEL response: ${domain}</strong> ${source}` : `<strong>PANEL response: ${domain}</strong>`
                        this.output.push(message)
                        this.progressPanel += 100 / domains.length
                    })
                }
                if (this.cgi) {
                    const request = {
                        connectionDetails: {
                            ip: this.ip,
                            password: this.password,
                            port: this.port
                        },
                        user: this.targetUser
                    };
                    (async () => {
                        try {
                            await AXIOS.post('/panel/check', request)
                            const addingRequest = getAddingRequest()
                            const domains = this.domains.split(/[ ,\n]+/)
                            addDomains(domains, addingRequest)
                        } catch (error) {
                            console.log(error)
                            this.showAlert(error)
                        }
                    })()
                } else {
                    const addingRequest = getAddingRequest()
                    const domains = this.domains.split(/[ ,\n]+/)
                    addDomains(domains, addingRequest)
                }
            },
            saveOutput: function() {
                document.location =
                    'data:text/attachment;,' +
                    this.output
            },
            sendRequest: function() {
                this.progressCf = 0
                this.progressPanel = 0
                const domains = this.domains.split(/[ ,\n]+/)
                const invalidFieldsMessage = 'Необходимо корректно заполнить обязательные поля!'
                if (domains.length > 100) {
                    this.showAlert('За один раз можно добавить не более 100 доменов!')
                    this.buttonDisabled = false
                    return
                }
                let stop = false
                domains.map(domain => {
                    if (!/.*[a-zA-Z0-9-]+\.[a-zA-Z0-9-]{2,}/.test(domain)) {
                        this.showAlert(`Недопустимое доменное имя ${domain}`)
                        this.buttonDisabled = false
                        stop = true
                    }
                })
                if (stop) {
                    return
                }
                const sendCfRequest = () => {
                    const request = {
                        apiKey: this.apiKey,
                        email: this.email,
                        jumpStart: this.jumpStart
                    }
                    domains.map(async domain => {
                        request.name = domain
                        this.buttonDisabled = true
                        try {
                            const result = await AXIOS.post('/cf/zone', request)
                            if (result.data.success) {
                                this.output.push(`<strong>CloudFlare response: ${domain}</strong> ${result.data.result.name_servers}`)
                                if (!this.jumpStart) {
                                    const zoneId = result.data.result.id
                                    const zoneDnsRecords = ['www']
                                    zoneDnsRecords.push(domain)
                                    zoneDnsRecords.map(async (record) => {
                                        const recordCreationRequest = {
                                            type: 'A',
                                            name: record,
                                            content: this.ip,
                                            proxied: true,
                                            apiKey: this.apiKey,
                                            email: this.email
                                        }
                                        console.log(recordCreationRequest)
                                        const answer = await AXIOS.post(`/cf/zone/${zoneId}`, recordCreationRequest)
                                        if (!answer.data.success) {
                                            answer.data.errors.map((err) => this.output.push(`<strong>CloudFlare response: ${domain}</strong> record ${record} error ${err.code} ${err.message}`))
                                        }
                                    })
                                }
                            } else {
                                result.data.errors.map(error => {
                                    this.output.push(`<strong>CloudFlare response: ${domain}</strong> ${error.code} ${error.message}`)
                                })
                            }
                        } catch (err) {
                            this.showAlert(err)
                        }
                        this.buttonDisabled = false
                        this.progressCf += 100 / domains.length
                    })
                }
                const sendGetPanelUsersRequest = async () => {
                    const connectionDetails = {
                        ip: this.ip,
                        password: this.password,
                        port: this.port
                    }
                    try {
                        this.buttonDisabled = true
                        const result = await AXIOS.post('/panel/users', connectionDetails)
                        if (result.status === 200 && result.data.length) {
                            this.users = result.data
                            this.userChooseOpen = true
                            this.buttonDisabled = false
                        }
                    } catch (error) {
                        this.showAlert(error)
                        this.buttonDisabled = false
                    }
                }
                if (this.toCf) {
                    if (!this.email || !this.apiKey || (!this.jumpStart && !/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(this.ip))) {
                        this.showAlert(invalidFieldsMessage)
                        return
                    }
                    sendCfRequest()
                }
                if (this.toPanel) {
                    if (!this.ip || !this.password || !this.port || !/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(this.ip)) {
                        this.showAlert(invalidFieldsMessage)
                        return
                    }
                    sendGetPanelUsersRequest()
                }
            },
            showAlert: function(error) {
                let content
                if (error.data) {
                    content = error.data.message || error.data
                } else content = error.toString() // just raw error string
                this.alertContent = content
                this.alertOpen = true
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .md-card {
        margin: 0 auto;
        width: 42%;
    }

    form {
        padding: 2em;
        text-align: left;
    }

    .right {
        float: right;
    }

    .output {
        width: 90%;
        padding: 1.5em;
        margin-bottom: 0.5em
    }
</style>