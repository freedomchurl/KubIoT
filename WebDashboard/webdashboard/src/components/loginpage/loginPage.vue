<template>
    <div>
        <LoginHeader></LoginHeader>
        <LoginForm></LoginForm>
    </div>
</template>

<script>
import LoginHeader from './loginHeader.vue'
import LoginForm from './loginForm.vue'

export default {
    components:{
        'LoginHeader':LoginHeader,
        'LoginForm':LoginForm
    }
}
</script>


<style scoped>

</style>