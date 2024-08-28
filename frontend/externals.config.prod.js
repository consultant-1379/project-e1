const externals = {
  apps: [{
    path: "app-1",
    entry: "App1"
  }],
  components: {
    default: [],
    shareable: [{
      path: "main-page",
      entry: "MainPage"
    }, {
      path: "ms-details-component",
      entry: "MsDetailsComponent"
    }, {
      path: "ms-addmenu-component",
      entry: "MsAddmenuComponent"
    }, {
      path: "dependency-graph-component",
      entry: "DependencyGraphComponent"
    }]
  },
  panels: [],
  plugins: []
};
module.exports = externals;