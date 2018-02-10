<template>
  <div id="vue-workflow-engine">
    <app-header></app-header>
    <b-table striped hover :items="workflowList" :fields="fields">
    </b-table>
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
  name: 'WorkflowEngine',
  components: { AppHeader, AppFooter },
  data () {
    return {
      fields: ['id', 'workflow_id', {'running_status': 'status'}],
      workflowList: []
    }
  },
  methods: {
    research: function () {
      let targetPath = baseUrl + '/workflow-status-groups'

      axios.get(targetPath).then(response => {
        this.workflowList = response.data
      }).catch(error => {
        console.log(error)
      })
    }
  },
  created: function () {
    this.research()
  }
}
</script>
