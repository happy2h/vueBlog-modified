<template>
<div>
  <div class="del-container">
    <el-result icon="warning" title="确定要删除吗" subTitle="删除后无法恢复">
      <template slot="extra">
        <el-button type="primary" size="medium" @click="del">确定</el-button>
        <el-button  size="medium" @click="back">取消</el-button>
      </template>
    </el-result>
  </div>
</div>
</template>

<script>
export default {
  name: "BlogDelete",
  methods: {
    del() {
      const blogId = this.$route.params.blogId //获取路径中的blogid参数
      console.log(blogId)
      this.$axios.post('/del/'+ blogId, '', { // 发送post请求，在请求头中封装当前用户的token信息
        headers: {
          "Authorization": localStorage.getItem('token')
        }
      }).then(res => {
        alert("删除成功")
        this.$router.push('/blogs') // 删除后进行跳转
      })
    },
    back() {// 用户取消返回之前的页面
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.del-container {
  margin: 0 auto;
}
</style>