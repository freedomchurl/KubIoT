<template>
    <div>
        <InitHeader></InitHeader>
        <InitForm></InitForm>
    </div>
</template>

<script>
import InitialHeaderVue from './InitialHeader.vue'
import InitialFormVue from './InitialForm.vue'


export default {
    components:{
    'InitHeader':InitialHeaderVue,
    'InitForm':InitialFormVue
    }
}
</script>

<style scoped>

</style>