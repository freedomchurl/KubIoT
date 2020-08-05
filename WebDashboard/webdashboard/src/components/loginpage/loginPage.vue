<template>
    <div>
        <!-- {{this.$route.params}} -->
        <!-- <LoginHeader v-bind:projectname=projectname></LoginHeader>
        <LoginForm></LoginForm> -->
        
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
    },
    data(){
        return{
            //projectName:''
        }
    },
    // props:{
    //     projectname:{
    //         default:''
    //     }
    // },
    // mounted(){
    //     //console.log(this.$route.params);
    // }
}
</script>


<style scoped>

</style>