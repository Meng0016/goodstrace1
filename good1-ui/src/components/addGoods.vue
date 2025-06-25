<script setup>
import axios from "axios";
import TraceDetail from "./TraceDetail.vue";
export default {
  components:{
    TraceDetail
  },
  data(){
    return{
      form: {
        traceName: '',
        traceNumber: '',
        location: '',
        quality: ''
      },
      formLabelWidth: '180px',
      scanData: '',
      testData: {
        '1000001' : {'traceName' : '供应商单位1','quality': '1', 'location':'供应地点1'},

      }
    }
  }
}
</script>

<template>
    <div>
      <el-dialog title="添加商品信息" :visible.sync="dialogFormVisible" :show-close="false" width="55%" :center="true"
                  custom-class="custom-dialog">
        <el-form :model="form" :ref="form" label-position="left" :label-width="formLabelWidth">
          <el-form-item label="质检情况" prop="quality" :rules="[{required : true,message : '请选择质检情况',trigger:'change'}]">
            <el-radio-group v-model="form.quality">
              <el-radio label="θ">优质</el-radio>
              <el-radio label="1">合格</el-radio>
              <el-radio label="2">不合格</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item Label="收货单位(本单位)" prop="traceName" :rules="[{required:true,message:'请输入收货单位'}]">
          <el-input v-model="form.traceName" autocomplete="off" class="rounded-input"></el-input>
          </el-form-item>
          <el-form-item label="地址" prop="Location" :rules="[{ required: true,message: '请输入单位地址'}]">
          <el-input v-model="form.location" autocomplete="off" class="rounded-input"></el-input>
          </el-form-item>
        </el-form>
        <div class="dialog-footer">
          <el-button @click="$emit('popup', false)" round>取 消</el-button>
          <el-button type="primary" @click="submitForm('form')" round>确 定</el-button>
        </div>
        <el-divider></el-divider>
        <TraceDetail :good-detail="good" :user="user" :block_pro="block_pro"/>
      </el-dialog>
    </div>
</template>

<style scoped>

</style>
