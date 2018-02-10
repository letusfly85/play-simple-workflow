<template>
  <div id="Order">
    <ol class="breadcrumb">
      <div v-for="workflow_status in workflow_statuses" v-bind:key="workflow_status.id">
        <li class="breadcrumb-item" style="margin-left:15px;">
          <a v-bind:href="'#' + workflow_status.path">{{ workflow_status.path }}</a>
        </li>
      </div>
    </ol>
    <div>
      <h3>Order</h3>
    </div>
    <b-form @submit="goNext">
      <b-button type="submit" class="btn-primary">Go Next Step</b-button>
    </b-form>
  </div>
</template>

<script>
import axios from 'axios'
const baseUrl = 'http://localhost:9000'
axios.defaults.xsrfHeaderName = 'Csrf-Token'
axios.defaults.xsrfCookieName = 'PLAY_CSRF_TOKEN'

export default {
  name: 'Order',
  data () {
    return {
      orders: [],
      workflow_statuses: [],
      user_id: 0,
      workflow_id: 0,
      workflow_step_id: 0
    }
  },
  methods: {
    goNext: function () {
      let targetPath = baseUrl + '/workflow-engines/routes'
      let params = {
        user_id: this.user_id,
        workflow_id: Number(this.workflow_id),
        workflow_step_id: Number(this.workflow_step_id)
      }

      event.preventDefault()
      axios.post(targetPath, params, {}).then(response => {
        console.log(response.data)
        this.$router.push(response.data.path)
      }).catch(function (error) {
        console.log(error)
      })
    },
    search: function () {
      let targetPath = baseUrl + '/orders'
      let self = this
      axios.get(targetPath, {}).then(response => {
        self.orders = response.data
      }).catch(function (error) {
        console.log(error)
      })

      targetPath = baseUrl + '/workflow-engines/belong?user_id=' + self.user_id
      axios.get(targetPath, {}).then(function (response) {
        console.log(response.data)
        self.workflow_id = response.data.workflow_id
        self.workflow_step_id = response.data.workflow_step_id
      }).catch(function (error) {
        console.log(error)
      })

      targetPath = baseUrl + '/workflow-statuses/list?user_id=' + this.user_id
      axios.get(targetPath, {}).then(function (response) {
        console.log(response.data)
        self.workflow_statuses = response.data
      }).catch(function (error) {
        console.log(error)
      })
    }
  },
  created: function () {
    this.search()
  }
}
</script>

<style scoped>

</style>
