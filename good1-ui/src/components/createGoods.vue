
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
    document.addEventListener('keydown', this.handleScannerInput);
  },
  beforeDestroy() {
    document.removeEventListener('keydown', this.handleScannerInput);
  },
  methods: {
    handleClose(){
      this.dialogFormVisible = false;
      this.$emit('popup', false);
    },
    handleScannerInput(e) {
      if (!this.scanData){
        this.scanData = ''
      }
      if (e.key !== 'Enter') {
        this.scanData += e.key;
      }else{
        this.fillFormData(this.scanData);
        this.scanData = '';
      }
    },
    fillFormData(traceNumber) {
      const data = this.testData[traceNumber];
      if (data) {
        data.traceNumber = parseInt(traceNumber);
        this.formSubmit(data);
      }else{
        console.error('没有该溯源码',traceNumber);
        alert('扫描的溯源码无效，请重新扫描！');
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.form)
          this.$emit('confirmPopup', this.form);
          alert('提交成功');
        } else {
          alert('提交失败，请检查输入');
          return false;
        }
      });
    },
    formSubmit(data){
      axios({
        method: 'post',
        url: '/produce',
        data: {
          ...data,
          quality: parseInt(data.quality),
          producerAddress: localStorage.getItem('account')
        }
      }).then(ret =>{
        if (ret.data.ret !== 1){
          if (ret.data.ret === 0 && ret.data.msg === 'traceNumber already exists'){
            this.$message({
              message: '该溯源码已存在',
              type: 'error',
              center: true
            });
          }else {
            this.$message({
              message: '提交失败',
              type: 'error',
              center: true
            });
          }
          return;
        }
        this.$blockInfo.setBlockInfo("Pro" + data.traceNumber,ret.data.data);
        console.log(this.$blockInfo.getBlockInfo("Pro" + data.traceNumber));
        this.$message({
          message: '提交成功',
          type: 'success',
          center: true
        });
      }).catch(err => {
        console.error(err);
      });
      this.popup = false;
    }

  }
}
</script>

<style scoped>
.custom-dialog {
  border-radius: 10px;
  border: 2px solid #409EFF;
  font-family: 'Arial', sans-serif;
}
.rounded-input {
  border-radius: 5px;
  border: 1px solid #409EFF;
  padding: 8px;
}
.dialog-footer {
  text-align: center;
  margin-top: 20px;
}
.dialog-footer .el-button {
  margin-right: 10px;
  border-radius: 5px;
}
</style>
