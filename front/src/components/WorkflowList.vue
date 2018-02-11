<template>
  <div id="vue-workflow-engine">
    <app-header></app-header>
    <b-card
      title="Workflow List"
      style="margin-top: 5px; margin-right: 5px; margin-left: 5px;"
    >
    <div v-for="workflow in workflowList" v-bind:key="workflow.id">
        <b-card
          :sub-title="'dependency is ' + String(workflow.before_workflow_id)"
          class="inactive-dependency-card"
          v-if="workflow.before_workflow_id"
        >
        </b-card>
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
          <br/>
          <b-form :id="String(workflow.workflow_id)" @submit="addDependency(workflow.workflow_id)">
            <b-button :pressed.sync="workflow.toggle" class="btn-outline-success">Dependency</b-button>
            <div v-if="workflow.toggle">
              <br/>
              <b-form-select
                v-model="selectedDependencyId"
                :options="workflow.otherIdList"
                @change="workflow.updateButton=true"
                class="mb-3" />
            </div>
            <div v-if="workflow.updateButton">
              <b-button type="submit" class="btn-success">Add Dependency</b-button>
            </div>
          </b-form>
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
      workflowList: [],
      selectedDependencyId: null
    }
  },
  methods: {
    research: function () {
      let targetPath = baseUrl + '/workflow-status-groups'

      axios.get(targetPath).then(response => {
        let workflowIdList = response.data.map(function (workflow) {
          return workflow.workflow_id
        })
        this.workflowList = response.data.map(function (workflow) {
          workflow.toggle = false
          workflow.updateButton = false
          workflow.otherIdList = []

          workflowIdList.forEach(function (id) {
            if (workflow.workflow_id !== id) {
              workflow.otherIdList.push(id)
            }
          })
          return workflow
        })
      }).catch(error => {
        console.log(error)
      })
    },
    addDependency: function (workflowId) {
      console.log(workflowId)
      console.log(this.selectedDependencyId)

      let targetPath = baseUrl + '/workflow-engine-groups'
      let params = {
        workflow_id: workflowId,
        before_workflow_id: this.selectedDependencyId
      }

      console.log(params)
      event.preventDefault()
      axios.put(targetPath, params).then(response => {
        console.log(response.data)
        this.research()
      }).catch(function (error) {
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
    width: 15rem;
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
    width: 15rem;
    margin-left: 20px;
    float: left;
  }

  .inactive-dependency-card {
    background-color: #ececf6;
    max-width: 20rem;
    width: 15rem;
    height: 2rem;
    opacity: 0.5;
    margin-bottom: 3px;
    margin-left: 20px;
  }
</style>
