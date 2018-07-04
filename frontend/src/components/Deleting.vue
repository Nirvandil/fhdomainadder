<template>
    <md-app>
        <md-app-content>
            <nav-tabs active="deleting"></nav-tabs>
            <md-card>
                <form>

                    <md-field>
                        <md-input v-model="form.ip" requried></md-input>
                        <label>IP адрес</label>
                    </md-field>

                    <md-field>
                        <md-input v-model.trim="form.port" placeholder="Порт SSH" required></md-input>
                        <label>3333 по умолчанию</label>
                    </md-field>

                    <md-field>
                        <md-input v-model.trim="form.password"></md-input>
                        <label>Пароль root</label>
                    </md-field>

                    <md-field>
                        <md-input v-model.trim="userName"></md-input>
                        <label>Имя пользователя панели</label>
                    </md-field>

                    <md-field>
                        <md-textarea required v-model.trim="domains"></md-textarea>
                        <label>Список доменов, по одному в строке</label>
                        <md-icon>description</md-icon>
                    </md-field>

                    <md-button class="md-raised md-accent" @click="deleteDomains" :disabled="buttonDisabled">
                        Удалить
                    </md-button>
                </form>
                <md-dialog-alert :md-active.sync="alertOpen" :md-content="alertContent" md-confirm-text="OK"/>
            </md-card>
            <md-card class="output" v-if="output.length">
                <ul style="text-align: left">
                    <li v-for="out in output" :key="out" v-html="out">
                    </li>
                </ul>
            </md-card>
        </md-app-content>
    </md-app>
</template>

<script>
    import NavTabs from './NavTabs'
    import {AXIOS} from './http-common'

    export default {
        name: 'deleting',
        components: {NavTabs},
        data() {
            return {
                form: {
                    ip: '',
                    port: 3333,
                    password: ''
                },
                userName: '',
                domains: '',
                buttonDisabled: false,
                alertOpen: false,
                alertContent: '',
                output: []
            }
        },
        methods: {
            deleteDomains: function() {
                if (!this.domains || !this.form.ip || !this.form.port || !this.form.password || !this.userName) {
                    this.alertContent = 'Необходимо заполнить обязательные поля!'
                    this.alertOpen = true
                } else {
                    this.buttonDisabled = true
                    const domains = this.domains.split(/[ ,\n]+/)
                    let stop = false
                    domains.map(domain => {
                        if (!/.*[a-zA-Z0-9-]+\.[a-zA-Z0-9-]{2,}/.test(domain)) {
                            this.alertContent = `Недопустимое доменное имя ${domain}`
                            this.buttonDisabled = false
                            this.alertOpen = true
                            stop = true
                        }
                    })
                    if (stop) {
                        return
                    }
                    domains.map(doman =>
                        AXIOS.post('/panel/delete', {
                            connectionDetails: this.form,
                            domain: doman,
                            userName: this.userName
                        })
                            .then(res => {
                                if (res.data) {
                                    this.output.push(`PANEL response for ${doman}: ${res.data}`)
                                } else {
                                    this.output.push(`${doman} deleted (or never exists).`)
                                }
                            })
                            .catch(err => {
                                this.alertOpen = true
                                this.alertContent = err.toString()
                                this.buttonDisabled = false
                            })
                    )
                    this.buttonDisabled = false
                }
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
