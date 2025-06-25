
<template>
  <div>
    <el-dialog title="新建商品" :visible.sync="dialogFormVisible" :show-close="false" width="35%" :center="true"
               @close="handleClose" custom-class="custom-dialog" >
      <el-form :model="form" ref="form" label-position="left" :label-width="formLabelwidth">
        <el-form-item Label="溯源码" prop="traceNumber" :rules="[{ required: true,message: '请输入溯源码'}]">
          <el-input v-model.number="form.traceNumber" autocomplete="off" class="rounded-input"></el-input>
        </el-form-item>
        <el-form-item label="商品名称" prop="goodsName" :ruLes="[{ required:true,message: '请输入商品名称'}]">
          <el-input v-model="form.goodsName" autocomplete="off" class="rounded-input"></el-input></el-form-item>
        <el-form-item Label="生产商" prop="traceName" :rules="[{ required:true,message: '请输入生产商'}]">
          <el-input v-model="form.traceName" autocomplete="off" class="rounded-input"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="Location" :rules="[{ required:true,message:'请输入商品地址'}]">
          <el-input v-model="form.location" autocomplete="off" class="rounded-input"></el-input>
        </el-form-item>
        <el-form-item Label="质检情况" prop="quality" :rules="[{ required:true,message:'请选择质检情况',trigger:'change'}]">
        <el-radio-group v-model="form.quality">
          <el-radio label="θ">优质</el-radio>
          <el-radio label="1">合格</el-radio>
          <el-radio label="2">不合格</el-radio>
        </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="$emit('popup', false)" round>取消</el-button>
        <el-button type="primary" @click="submitForm('form')" round>确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import axios from "axios";
export default {
  data() {
    return {
      form: {
        goodsName: '',
        traceNumber: '',
        traceName: '',
        location: '',
        quality: ''
      },
      formLabelWidth: '120px',
      scanData: '',
      testData: {
        '1000001' : {'goodsName': '商品1', 'traceName': '生产商1', 'location': '地址1', 'quality': 'θ'},
        '1000002' : {'goodsName': '商品2', 'traceName': '生产商2', 'location': '地址2', 'quality': '1'},
        '1000003' : {'goodsName': '商品3', 'traceName': '生产商3', 'location': '地址3', 'quality': '2'},
        '1000004' : {'goodsName': '商品4', 'traceName': '生产商4', 'location': '地址4', 'quality': 'θ'},
        '1000005' : {'goodsName': '商品5', 'traceName': '生产商5', 'location': '地址5', 'quality': '1'},
        '1000006' : {'goodsName': '商品6', 'traceName': '生产商6', 'location': '地址6', 'quality': '2'},
        '1000007' : {'goodsName': '商品7', 'traceName': '生产商7', 'location': '地址7', 'quality': 'θ'},
        '1000008' : {'goodsName': '商品8', 'traceName': '生产商8', 'location': '地址8', 'quality': '1'},
        '1000009' : {'goodsName': '商品9', 'traceName': '生产商9', 'location': '地址9', 'quality': '2'},
        '1000010' : {'goodsName': '商品10', 'traceName': '生产商10', 'location': '地址10', 'quality': 'θ'}
      }
    }
  },
  props: ['dialogFormVisible'],
  mounted() {
    document.addEventListener()
  }
}
</script>

<style scoped>

</style>
