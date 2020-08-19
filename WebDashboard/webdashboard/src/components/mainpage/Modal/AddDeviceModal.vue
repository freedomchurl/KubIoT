<template>
  <div id="scroll-box" class="container">
    <div id="title-box">그룹 내 장치 추가</div>
    
    <!-- <input class="btn btn-primary col-md-3" @click="del_data" type="button" value="메모 수정"> -->

    <div id="device-control-box">
      <span>그룹 멤버 :</span>
      <!-- <textarea v-model="device_memo" v-bind:placeholder="place_device"></textarea> -->
      <select v-model="selected" @change="switchView($event, $event.target.selectedIndex)">
        <option disabled value>Please select one</option>
        <option v-on:change="getindex(index)" v-for="(device,index) in dataset" v-bind:key="index">{{device.name}}</option>
      </select>
      <input
        id="control-btn"
        class="btn btn-primary col-md-3"
        @click="adddevice_togroup"
        type="button"
        value="장치 추가"
      />
    </div>

    <table class="table table-hover">
        <thead>
          <tr>
            <th scope="col">장치 ID</th>
            <th scope="col">이름</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(data,index) in selectDID" v-bind:key="index">
            <td>{{data.id}}</td>
            <td>{{data.name}}</td>
          </tr>
        </tbody>
      </table>

    <input
      id="control-btn"
      class="btn btn-primary col-md-3"
      @click="groupCreate"
      type="button"
      value="생성"
    />

    <!-- <div class="input-group col-md-3">
            <span class = "input-group-addon">
                {{hot_table}}
            </span>
            <input type="text" v-model="del_password" class="form-control col-md-2">
        </div>
        <div class="row col-md-6"> 
            <input class="btn btn-default col-md-3" @click="del_data" type="button" value="삭제">
    <input class="btn btn-default col-md-3" @click="$emit('close')" type="button" value="취소">-->
    <!-- </div> -->
  </div>
</template>
<script>
import axios from 'axios'
import IP from '../../../../static/IP'
export default {
  data: function () {
    return {
    //   device: this.device_info,
    //   device_memo: this.device_info.memo,
    //   place_device: this.device_info.memo,
        groupInfo:this.group_info,
      textinput: "form-control",
      dataset:[{name:'ss'},{name:'dd'}],
      selected:'',
      selectedIndex:'',
      selectDID:[],
    };
  },
  created() {
      var vm = this;
    axios
      .get("http://" + IP.IP + ":7676/device/info", {
        params: { pageinfo: 1 },
        timeout: 1000, // 1초 이내에 응답이 없으면 에러 처리
      })
      .then((res) => {
        console.log(res.data);

        vm.dataset = res.data.payload;
      });
  },
  props: ["group_info"],
  methods: {
      groupCreate(){
        var did = [];
        for(var i=0;i<this.selectDID.length;i++)
        {
            did.push(this.selectDID[i].id); // 다 집어 넣고
        }
        //var gName = this.groupname;
        var vm=this;
        axios.post("http://" + IP.IP + ":7676/device/info/addDeviceongroup", {
              gID:vm.groupInfo.id , dID:did,
            })
            .then((res) => {
              console.log(res.status + 'aaa');
              if(res.status==200)
              {
                  vm.$emit("close");
              }
            });


      },
      adddevice_togroup(){
          // 여기서 이번에 고른 디바이스를 추가한다. 아래에다가, 밑에다가.
          console.log('index ' + this.selected);
          this.selectDID.push({id:this.dataset[this.selectedIndex-1].id,name:this.dataset[this.selectedIndex-1].name})
          console.log(this.selectDID);
      },
      getindex(index){
          console.log(this.selected + " Hmm");
          console.log(index + " why");
          this.selectedIndex = index;
      },
      switchView: function(event, selectedIndex) {
      console.log(event, selectedIndex);      
      this.selectedIndex = selectedIndex;
    },
    del_data() {
      this.$emit("close");
    },
  },
};
</script>
<style scoped>
#title-box {
  background-color: rgb(190, 190, 190);
  margin: 0;
  font-size: 1.2rem;
  font-weight: bold;
  padding: 1rem 0rem 1rem 2rem;
  text-align: left;
  position: relative;
  top: 0px;
}
#memobox {
  width: 100%;
  height: 50px;
  padding: 1rem 2rem 0 0;
  margin: 1rem 0rem 1rem 0rem;
  display: flex;
  align-items: center;
}
#device-control-box {
  width: 100%;
  height: 50px;
  padding: 1rem 2rem 0 0;
  margin: 1rem 0rem 1rem 0rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
select {
  height: 100%;
  width: 50%;
}
#group-name {
  width: 100%;
}
#control-btn {
  /* margin-bottom: 2rem; */
}
span {
  width: 20%;
  margin-right: 1rem;
  font-size: 1.2rem;
  font-weight: bold;
}
textarea {
  width: 80%;
  height: 100%;
}
div#scroll-box {
  margin: 0 0 0 0;
  padding: 0 0 0 0;
  /* padding: 1rem 2rem 0 0; */
  max-height: 600px;
  max-width: 800px;
  overflow-y: scroll;
  /* height: 600px; */
}
</style>