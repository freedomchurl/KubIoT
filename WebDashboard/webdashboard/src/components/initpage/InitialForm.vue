<template>
    <div>
        <input type="text" v-model="newProjectName" placeholder="프로젝트명"><br>
        <input type="text" v-model="newProjectID" placeholder="관리자 계정명"><br>
        <input type="password" v-model="newProjectPass" placeholder="관리자 계정 비밀번호"><br>
        <input type="password" v-model="newProjectPassAgain" placeholder="관리자 계정 비밀번호 확인"><br>
        <button v-bind:class="primebtn" @click="addProject">프로젝트 생성</button> <!--얘가 여기 말고 다른 곳에 있을 경우, props / emit으로 데이터를 주고받아야함 --> 
    </div>
</template>

<script>
export default{
    data(){
        return{
            newProjectName:'',
            newProjectID:'',
            newProjectPass:'',
            newProjectPassAgain:'',
            primebtn:'btn btn-primary'
        }
    },
    methods:{
        addProject(){
            console.log(this.newProjectName + ' ' + this.newProjectID + ' ' + this.newProjectPass + ' ' + this.newProjectPassAgain);
            // 이 부분에, 암호화를 거쳐서, MySQL에다가 저장해야한다.
        }
    }
}
</script>

<style>
</style>