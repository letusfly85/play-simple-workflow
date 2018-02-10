<template>
  <div id="Item">
    <app-header></app-header>
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <div v-for="workflow_status in workflow_statuses" v-bind:key="workflow_status.id">
          <li class="breadcrumb-item" style="margin-left:15px;">
            <a v-bind:href="'#' + workflow_status.path">{{ workflow_status.path }}</a>
          </li>
        </div>
      </ol>
    </nav>
    <div>
      <h3>Item</h3>
    </div>
    <b-form @submit="goNext">
      <b-button type="submit" class="btn-primary">Go Next Step</b-button>
    </b-form>
    <app-footer></app-footer>
  </div>
</template>

<script>
import axios from 'axios'
import AppHeader from './utils/AppHeader'
import AppFooter from './utils/AppFooter'
const baseUrl = 'http://localhost:9000'
axios.defaults.xsrfHeaderName = 'Csrf-Token'
axios.defaults.xsrfCookieName = 'PLAY_CSRF_TOKEN'

export default {
  name: 'Item',
  components: { AppHeader, AppFooter },
  data () {
    return {
      items: [],
      workflow_statuses: [],
      user_id: 0,
      workflow_id: 0,
      workflow_step_id: 0
    }
  },
  methods: {
    goNext: function (event) {
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
      let targetPath = baseUrl + '/items'
      let self = this
      axios.get(targetPath, {}).then(response => {
        self.items = response.data
      }).catch(function (error) {
        console.log(error)
      })

      targetPath = baseUrl + '/workflow-statuses?user_id=' + this.user_id
      axios.get(targetPath, {}).then(function (response) {
        const workflowId = response.data[0].workflow_id
        targetPath = baseUrl + '/workflow-engines/belong/ ' + workflowId + '?user_id=' + self.user_id

        axios.get(targetPath, {}).then(function (response) {
          console.log(response.data)
          self.workflow_id = response.data.workflow_id
          self.workflow_step_id = response.data.workflow_step_id
        }).catch(function (error) {
          console.log(error)
        })
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
