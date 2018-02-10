<template>
  <div id="vue-workflow-engine">
    <app-header></app-header>

    <b-card
      title="Workflow Scheme"
      style="margin-top: 5px; margin-right: 5px; margin-left: 5px;"
    >
    <b-table striped hover :items="workflowEngines" :fields="fields">
      <template slot="path" slot-scope="row">
        <div v-if="row.item.editable">
          <b-row class="mb-2">
            <b-form-select v-model="form.path"
                           :options="options" class="mb-3"
                           @change="row.item.changed=true;">
            </b-form-select>
          </b-row>
        </div>
        <div v-if="!row.item.editable">
          <div v-text="row.item.path" @click="row.item.editable = true" />
        </div>
      </template>
      <template slot="update" slot-scope="row">
        <div v-if="row.item.changed">
          <b-form @@submit="updateRecord">
            <b-row class="sr-only">
              <b-form-input v-bind:value="`${form.wid = row.item.id}`">
              </b-form-input>
              <b-form-input id="workflow_id" v-bind:value="`${form.workflow_id = row.item.workflow_id}`">
              </b-form-input>
              <b-form-input id="step_id"
                            v-bind:value="`${form.step_id = row.item.step_id}`">
              </b-form-input>
              <b-form-input id="is_first_step"
                            v-bind:value="`${form.is_first_step = row.item.is_first_step}`">
              </b-form-input>
              <b-form-input id="is_last_step"
                            v-bind:value="`${form.is_last_step = row.item.is_last_step}`">
              </b-form-input>
            </b-row>
            <b-button type="submit" class="btn-success">update</b-button>
          </b-form>
        </div>
        <div v-if="!row.item.changed">
          <b-form @submit="destroyRecord(row.item.id)">
            <b-button type="submit" class="btn-danger">destroy</b-button>
          </b-form>
        </div>
      </template>
    </b-table>
    <button v-on:click="toggleChange(addToggle)" class="btn-outline-success">
      {{ addToggle ? 'Cancel' : ''}} Add Workflow Record
    </button>
    <br/>
    <br/>
    <b-form @submit="createRecord">
      <div v-if="addToggle">
        <b-form-input value="" v-model="form.workflow_id" class="form-control"></b-form-input>
        <br/>
        <b-form-select v-model="form.path" :options="options" class="mb-3"></b-form-select>
        <b-form-input value="" v-model="form.step_id" class="form-control"></b-form-input>
        <b-form-checkbox value="" v-model="form.is_first_step" class="form-control"></b-form-checkbox>
        <b-form-checkbox value="" v-model="form.is_last_step" class="form-control"></b-form-checkbox>
        <br/>
        <br/>
        <b-button type="submit" class="btn-success">Add Workflow Record</b-button>
      </div>
    </b-form>
    <br/>
    <br/>
    <b-form @submit="destroyAllRecord">
      <b-button type="submit" class="btn-danger">Destroy All Workflow Record</b-button>
    </b-form>
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
  name: 'WorkflowEngine',
  components: { AppHeader, AppFooter },
  data () {
    return {
      options: ['/orders', '/items', '/attachments'],
      fields: ['id', 'workflow_id', 'path', 'step_id', 'is_first_step', 'is_last_step', 'update'],
      addToggle: false,
      workflowEngines: [],
      deleteRowId: 0,
      form: {
        wid: 0,
        workflow_id: '0',
        path: '/',
        step_id: 0,
        is_first_step: false,
        is_last_step: false
      }
    }
  },
  methods: {
    toggleChange: function (toggle) {
      if (toggle === true) {
        this.addToggle = false
      } else {
        this.addToggle = true
      }
    },
    research: function () {
      let targetPath = baseUrl + '/workflow-engines'

      axios.get(targetPath).then(response => {
        this.workflowEngines = response.data.map(function (engine) {
          engine.wid = engine.id
          engine.editable = false
          engine.changed = false
          engine.update = false

          return engine
        })
      }).catch(error => {
        console.log(error)
      })
    },
    createRecord: function () {
      var params = {
        id: 0, // dummy value
        workflow_id: Number(this.form.workflow_id),
        path: this.form.path,
        step_id: Number(this.form.step_id),
        is_first_step: this.form.is_first_step,
        is_last_step: this.form.is_last_step
      }
      console.log(params)

      var targetPath = baseUrl + '/workflow-engines'
      axios.post(targetPath, params, {}).then(response => {
        this.research()
      }).catch(error => {
        console.log(error)
      })
    },
    updateRecord: function () {
      var params = {
        id: Number(this.form.wid),
        workflow_id: Number(this.form.workflow_id),
        path: this.form.path,
        step_id: Number(this.form.step_id),
        is_first_step: this.form.is_first_step,
        is_last_step: this.form.is_last_step
      }

      var targetPath = baseUrl + '/workflow-engines'
      axios.put(targetPath, params, {}).then(response => {
        this.research()
      }).catch(error => {
        console.log(error)
      })
    },
    destroyRecord: function (deleteId) {
      var targetPath = baseUrl + '/workflow-engines' + '/' + deleteId
      axios.delete(targetPath, {}).then(response => {
        this.research()
      }).catch(error => {
        console.log(error)
      })
    },
    destroyAllRecord: function () {
      var targetPath = baseUrl + '/workflow-engines/all' + '/' + '0'
      axios.delete(targetPath, {}).then(response => {
        this.research()
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
