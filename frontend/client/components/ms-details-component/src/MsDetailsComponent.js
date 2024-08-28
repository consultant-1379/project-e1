/**
 * Component MsDetailsComponent is defined as
 * `<e-ms-details-component>`
 *
 * Imperatively create component
 * @example
 * let component = new MsDetailsComponent();
 *
 * Declaratively create component
 * @example
 * <e-ms-details-component></e-ms-details-component>
 *
 * @extends {LitComponent}
 */
import { definition } from '@eui/component';
import { LitComponent, html } from '@eui/lit-component';
import style from './msDetailsComponent.css';
import '@eui/layout';
import 'main-page';
import 'dependency-graph-component';

/**
 * @property {Boolean} propOne - show active/inactive state.
 * @property {String} propTwo - shows the "Hello World" string.
 */
@definition('e-ms-details-component', {
  style,
  home: 'ms-details-component',
  props: {
    showContent: { attribute: true, type: Boolean, default: true },
    showDetailContent: { attribute: true, type: Boolean, default: true },
    loading: { attribute: true, type: Boolean, default: true },
    responseData: {attribute: true,type: Array, default:[] },
    graph: { attribute: true, type: Boolean, default: false },
    msname: { attribute: true, type: String },
    msDescription: { attribute: true, type: String, default: 'Create the Microservice, Version, and LeadEngineer models and interfaces for the Spring Boot application' },
    msCatagory: { attribute: true, type: String, default: 'Database' },
    msCurrentVersion: { attribute: true, type: String, default: 'V0.0.0' },
    msLeadName: { attribute: true, type: String, default: 'Sean Mcgregor' },
    msLeadEmail: { attribute: true, type: String, default: 'Email.....' },
    msDependencies: { attribute: true, type: Array, default: [] },
    msVersions: { attribute: true, type: Array, default: [] },
    msVersionsTable: { attribute: true, type: Array, default: [] },
    msVersionsMenu: { attribute: true, type: Array, default: [] },
    msVersionsMenuItems: { attribute: true, type: Array, default: [] },
    msDependency:  { attribute: true, type: String, default: "" },
    msDependencyVersion:  { attribute: true, type: String, default: "" },
    contents: { attribute: true, type: String }

  },
})
export default class MsDetailsComponent extends LitComponent {
  /**
   * Render the <e-ms-details-component> component. This function is called each time a
   * prop changes.
   */

  constructor() {
    super();
    // this.graphToggelerHandler(this.graph);
  }

  // connectedCallback() {
  //   super.connectedCallback();
  //   this._getMicroservice(this.getId(this.msname))
  //   // if (this.responseData){
  //   //   console.log(this.responseData)
  //   //   this.setData(this.responseData)
  //   // }
  // }
  _getMicroservice(name) {
    fetch("http://localhost:8080/microservices/" + name)
      .then((response) => response.json())
      .then((data) => {
        console.log(data.name);
        this.msname = data.name
        this.msDescription = data.currentVersion.description
        this.msCatagory = data.category
        console.log(this.msCatagory)
        this.currentVersion = this.versionNumberBuild(data.currentVersion.versionNumber)
        console.log(this.currentVersion)
        this.msLeadName = data.currentVersion.leadEngineer.name
        console.log(this.msLeadName)
        this.msLeadEmail = data.currentVersion.leadEngineer.email
        this.msDependencies = data.currentVersion.dependencies
        this.msVersions = data.versionNames
        console.log(this.msVersions)
        this.getTableItems()

        this.executeRender()
        // this.setData(data)
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }

  _postMsVesrion() {

    const postData = {
      category: this.shadowRoot.querySelector('eui-base-v0-text-field#msCatagory').value,
      versionNumber: {
        major: this.shadowRoot.querySelector('eui-base-v0-spinner#major').value,
        minor: this.shadowRoot.querySelector('eui-base-v0-spinner#minor').value,
        patch: this.shadowRoot.querySelector('eui-base-v0-spinner#patch').value
      },
      description: this.shadowRoot.querySelector('eui-base-v0-textarea#msDescription').value,
      leadEngineer: {
        name: this.shadowRoot.querySelector('eui-base-v0-text-field#msLeadName').value,
        email: this.shadowRoot.querySelector('eui-base-v0-text-field#msLeadEmail').value
      }
    }
    console.log(JSON.stringify(postData))
    fetch("http://localhost:8080/microservices/" + this.getId(this.msname) + "/versions", {

      // Adding method type
      method: "POST",
      // Adding headers to the request
      headers: {
        "Content-type": "application/json"
      },
      // Adding body or contents to send
      body: JSON.stringify(postData)
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Success:", data);
      });
  }

  async _getMicroservices() {
    await fetch("http://localhost:8080/microservices")
      .then((response) => response.json())
      .then((data) => {
        console.log("Success:", data);
        this.responseData = data;
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }

  _getDependencyVersions(name) {
    fetch("http://localhost:8080/microservices/" + this.getId(name) + "/versions")
      .then((response) => response.json())
      .then((data) => {
        this.msVersionsMenu = data
        console.log(this.msVersions)
        this.getMenuItems()
        console.log(data)
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }

  async _getDependency(name, version) {
    await fetch("http://localhost:8080/microservices/" + this.getId(name) + "/versions/" + version)
      .then((response) => response.json())
      .then((data) => {
        console.log(data)
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }

  getDependenciesBody() {
    let dependencies = []
    this.msDependencies.forEach(async element => {
      dependencies.push(await this._getDependency(element.col1, element.col2))
    })
    console.log(dependencies)
    return dependencies
  }


  versionNumberBuild(element) {
    let number = String(element.major) + '.' + String(element.minor) + '.' + String(element.patch)
    return number;
  }

  graphToggelerHandler(graph) {
    if (graph == true) {
      this.contents = `<eui-table-v0 tiny .columns=${dependencyColumns}></eui-table-v0>`
    }
    else {
      this.contents = "Graph coming soon!!!";

    }

  }

  getId(name) {
    let id = name.toLowerCase().replace(/ /g, '_');
    return id
  }

  didConnect = async () => {
    await this._getMicroservice(this.getId(this.msname));
    await this._getMicroservices()
    this.executeRender();
  };

  getMenuItems(){ 
    let counter = 0
    this.msVersionsMenu.forEach(element => {
      counter+=1
      this.msVersionsMenuItems.push({"label":element,"value":'item-'+String(counter)})
    })
    console.log(this.msVersionsMenuItems)
  }

  getTableItems() {
    this.msVersions.forEach(element => {
      this.msVersionsTable.push({ col1: element })
    });
  }

  // didChangeProps(changedProps){
  //   if (changedProps.has('msLeadEmail')) {

  //   }}

  render() {
    // console.log(this.getId(this.msname))
    // this._getMicroservice(this.getId(this.msname))
    // this.setData(this.responseData)
    const dependencyColumns = [
      { title: 'Dependency Name', attribute: 'col1' },
      { title: 'Version', attribute: 'col2' }
    ];
    const versionColumns = [
      { title: 'Version', attribute: 'col1' },
    ];

    let dependencies = [];
    let counter = 0
    this.responseData.forEach(element => {
      counter+=1
      dependencies.push({"label":element.name,"value":'item-'+String(counter)})
    })

    const addVersionMenu = html`
        <eui-base-v0-textarea fullwidth id='msDescription' name="item" cols="20" placeholder="Description">
        </eui-base-v0-textarea>
        <eui-base-v0-text-field fullwidth id='msLeadName' name="msLeadName" placeholder="Lead Engineer Name"
          success-message="Valid Name">
        </eui-base-v0-text-field>
        <eui-base-v0-text-field fullwidth id='msLeadEmail' name="msLeadEmail" placeholder="Lead Engineer Email"
          success-message="Valid Email">
        </eui-base-v0-text-field>
        <eui-base-v0-text-field size="41" id='msCatagory' name="msCatagory" placeholder="Catagory"></eui-base-v0-text-field>
        <eui-base-v0-info-popup message="Eg. Database, Messaging, Application" position="bottom-end"></eui-base-v0-info-popup>
        <p>Version:</p>
        <div width="100%">
          <p>Major:</p>
          <eui-base-v0-spinner id='major' value="0" @change="${(event) => console.log(event.detail.value)}">
          </eui-base-v0-spinner>
        </div>
        <div width="100%">
          <p>Minor:</p>
          <eui-base-v0-spinner id='minor' value="0" @change="${(event) => console.log(event.detail.value)}">
          </eui-base-v0-spinner>
        </div>
        <div width="100%">
          <p>Patch:</p>
          <eui-base-v0-spinner id="patch" value="0" @change="${(event) => console.log(event.detail.value)}">
          </eui-base-v0-spinner>
        </div>
        <div id="divDep">
        <eui-base-v0-dropdown @eui-dropdown:click="${ async (event) => {
            event.target.data.forEach(element => {if(element.checked===true){this.msDependency = element.label; console.log(this.msDependency)}})
            console.log(this.msDependency)
            await this._getDependencyVersions(this.msDependency)
            // this.getMenuItems()
            }}" .data=${dependencies} data-type="single" no-result-label="No Result Found" label="Dependency Name" width="20" >
          
        </eui-base-v0-dropdown>
        <eui-base-v0-dropdown @eui-dropdown:click=${ async (event) => {event.target.data.forEach(element => {if(element.checked===true){this.msDependencyVersion = element.label; console.log(this.msDependencyVersion)}})}} .data=${this.msVersionsMenuItems} data-type="single" no-result-label="No Result Found" label="Dependency Version"
            width="30"></eui-base-v0-dropdown>
          <eui-base-v0-button @click=${(e) => {this.msDependencies.push({col1: this.msDependency,col2: this.msDependencyVersion}); console.log(this.msDependencies)}} id="add-button" icon="plus" --btn-font-size></eui-base-v0-button>
        </div>
        
        <eui-table-v0 tiny .data=${this.msDependencies} .columns=${dependencyColumns}></eui-table-v0>
    `
    const tabs = html`
      <eui-layout-v0-tabs>
        <eui-layout-v0-tab selected>
          <eui-v0-icon name="routing"></eui-v0-icon>
          <label>Graph</label>
        </eui-layout-v0-tab>
        <eui-layout-v0-tab>
          <eui-v0-icon name="table"></eui-v0-icon>
          <label>Table</label>
        </eui-layout-v0-tab>
      
        <div id='graph' slot="content" selected>
          <e-dependency-graph-component></e-dependency-graph-component>
        </div>
      
        <div slot="content">
          <eui-table-v0 tiny .columns=${dependencyColumns}></eui-table-v0>
        </div>
      </eui-layout-v0-tabs>
    `

    if (this.showContent == true) {
      console.log("render")
      // console.log(this.responseData)
      // this.setData(this.responseData)
      return html`
      <eui-layout-v0-multi-panel-tile maximize id="multi-panel-2" tile-title="Microservice Details">
        <div slot="content">
          <div class="layout__dashboard">
            <!-- Content for multi panel tile goes here -->
            <eui-layout-v0-tile tile-title=${this.msname}, subtitle="Current Version: ${this.currentVersion}" column=0
              column-span=2>
              <div slot="content">
                <eui-base-v0-tag size="large">${this.msCatagory}</eui-base-v0-tag>
                <p>
                  ${this.msDescription}
                </p>
              </div>
            </eui-layout-v0-tile>
            <eui-layout-v0-tile tile-title="Lead Engineer Details:" column=0 column-span=1>
              <div slot="content">
                <p>${this.msLeadName}</p>
                <p>${this.msLeadEmail}</p>
      
              </div>
            </eui-layout-v0-tile>
            <eui-layout-v0-tile id="seany" row column=0 column-span=3>
              <div slot="content">
                <div id='tab' style="width: 65%; float: left;">
                  ${tabs}
      
                </div>
                <div style="width: 30%; float: right;">
                  <eui-table-v0 tiny .data=${this.msVersionsTable} .columns=${versionColumns}></eui-table-v0>
                </div>
              </div>
            </eui-layout-v0-tile>
          </div>
        </div>
        <eui-base-v0-button id="exit" @click=${() => this.showContent = false} alignEdge slot="left">
          <eui-v0-icon name="arrow-left"></eui-v0-icon>
        </eui-base-v0-button>
      
        <eui-layout-v0-tile-panel id="addVersion" tile-title="Add Microservice Version" slot="right" icon-name="plus"
          width=400>
          <div slot="content">
            ${addVersionMenu}
          </div>
          <eui-base-v0-button slot="footer" primary @click=${(event)=> {
          this._postMsVesrion();
          this.didConnect()
        }}
            >Submit</eui-base-v0-button>
        </eui-layout-v0-tile-panel>
      </eui-layout-v0-multi-panel-tile>
      `;
    }
    else {
      return html`<e-main-page></e-main-page>`
    }

  }
}

/**
 * Register the component as e-ms-details-component.
 * Registration can be done at a later time and with a different name
 */
MsDetailsComponent.register();
