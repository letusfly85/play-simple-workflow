<template>
  <div id="app-header">
  <b-navbar toggleable="md" type="dark" variant="info">

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-navbar-brand href="#">Simple Workflow</b-navbar-brand>

    <b-collapse is-nav id="nav_collapse">

      <b-navbar-nav>
        <b-nav-item href="#/workflow-engines">Workflow Scheme</b-nav-item>
        <b-nav-item href="#/workflow-list">Workflow List</b-nav-item>
        <!--<b-nav-item href="#" disabled>Disabled</b-nav-item> -->
      </b-navbar-nav>

      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">

        <!--
        <b-nav-form>
          <b-form-input size="sm" class="mr-sm-2" type="text" placeholder="Search"/>
          <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
        </b-nav-form>
        -->

        <b-nav-item-dropdown text="Lang" right>
          <b-dropdown-item href="#">EN</b-dropdown-item>
          <b-dropdown-item href="#">JA</b-dropdown-item>
        </b-nav-item-dropdown>

        <b-nav-item-dropdown right>
          <!-- Using button-content slot -->
          <template slot="button-content">
            <em>User</em>
          </template>
          <b-dropdown-item href="#">Profile</b-dropdown-item>
          <b-dropdown-item href="#/password/change">Password Change</b-dropdown-item>
          <b-dropdown-item-button v-on:click="signOut">Sign Out</b-dropdown-item-button>
        </b-nav-item-dropdown>
      </b-navbar-nav>

    </b-collapse>
  </b-navbar>
  </div>
</template>

<script>
import ApiClient from './ApiClient'
import Router from 'vue-router'

export default {
  name: 'SignOut',
  methods: {
    signOut: function () {
      var router = new Router()

      let targetPath = '/signOut'
      ApiClient.search(targetPath, () => {
        router.go('/signIn')
      }, (error) => {
        console.log(error)
        router.go('/signIn')
      })
    }
  }
}
</script>
