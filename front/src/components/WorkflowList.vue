<template>
  <div id="vue-workflow-engine">
    <app-header></app-header>
    <br/>
    <div v-for="workflow in workflowList" v-bind:key="workflow.workflow_id">
        <b-card
          :title="String(workflow.workflow_id)"
          style="max-width: 20rem; margin-left: 20px;"
          class="mb-4">

          <div v-if="workflow.running_status===0">
            <b-button href="#" variant="primary">Continue Workflow</b-button>
          </div>
          <div v-if="!(workflow.running_status===0)">
            <b-button href="#" class="btn-secondary" disabled>Your Workflow Done</b-button>
          </div>
        </b-card>
    </div>
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
