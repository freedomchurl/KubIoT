<template>
    <div id="login-box">
        <div id="left-box">
            <h2 id="title">Welcome to KubIoT!</h2>
            <p id="metioned">Sign In the project</p>
        </div>
        <div id="main-box">
        <LoginHeader></LoginHeader>
        <LoginForm></LoginForm>
        </div>
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
    created(){
        console.log('Login Page Created');
    }
}
</script>


<style scoped>
    html,body{
        position: absolute;
        height: 100%;
        
    }
    h2,p{
        color:wheat;     
    }
    h2{
        margin-top: 50%;
    }
    #left-box{
        width: 40%;
        height: 100%;
        background-color: rgb(42, 63, 84);
    }
    #main-box{
        width: 60%;
    }
    #login-box{
        position:absolute;
        display: flex;
        height: 100%;
        width: 100%;
        align-content: space-between;
    }
</style>