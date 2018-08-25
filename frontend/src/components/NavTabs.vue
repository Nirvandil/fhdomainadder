<template>
    <md-tabs :md-active-tab="active">
        <md-tab id="adding" to="/" md-label="Добавление"></md-tab>
        <md-tab id="deleting" to="deleting" md-label="Удаление"></md-tab>
    </md-tabs>
</template>

<script>
export default {
  name: 'NavTabs',
  props: ['active']
}
</script>

<style scoped>

</style>