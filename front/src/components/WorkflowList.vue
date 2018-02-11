<template>
  <div id="vue-workflow-engine">
    <app-header></app-header>
    <b-card
      title="Workflow List"
      style="margin-top: 5px; margin-right: 5px; margin-left: 5px;"
    >
    <div v-for="workflow in workflowList" v-bind:key="workflow.id">
        <b-card
          :title="String(workflow.workflow_id)"
          :class="[workflow.is_current? 'active-card' : 'inactive-card']"
        >

          <div v-if="workflow.running_status===0">
            <b-form @submit="continueWorkflow(workflow.workflow_id)">
              <b-button type="submit"  variant="primary">Continue Workflow</b-button>
            </b-form>
          </div>
          <div v-if="!(workflow.running_status===0)">
            <b-button href="#" class="btn-secondary" disabled>Your Workflow Done</b-button>
          </div>
        </b-card>
    </div>
    </b-card>
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
  name: 'WorkflowList',
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
    },
    continueWorkflow: function (workflowId) {
      let targetPath = baseUrl + '/workflow-engines/init?workflow-id=' + workflowId

      event.preventDefault()
      axios.get(targetPath, {}).then(response => {
        console.log(response.data.path)
        this.$router.push(response.data.path)
      }).catch(function (error) {
        console.log(error)
      })
    }
  },
  created: function () {
    this.research()
  }
}
</script>
<style>
  .active-card {
    background-color: #9fcdff;
    max-width: 20rem;
    margin-left: 20px;
    float: left;
  }
  .active-card:hover {
    background-color: mediumturquoise;
    opacity: 0.7;
  }

  .inactive-card {
    background-color: #ced4da;
    opacity: 0.7;
    max-width: 20rem;
    margin-left: 20px;
    float: left;
  }
</style>
