/**
 * Component MainPage is defined as
 * `<e-main-page>`
 *
 * Imperatively create component
 * @example
 * let component = new MainPage();
 *
 * Declaratively create component
 * @example
 * <e-main-page></e-main-page>
 *
 * @extends {LitComponent}
 */
import { definition } from '@eui/component';
import { LitComponent, html } from '@eui/lit-component';
import style from './mainPage.css';
import '@eui/layout';
import '@eui/table';
import 'ms-details-component';


@definition('e-main-page', {
  style,
  home: 'main-page',
  props: {
    showContent: { attribute: true, type: Boolean, default: true },
    showDetailContent: { attribute: true, type: Boolean, default: true },
    responseData: {attribute: true,type: Array, default:[] },
    msName: { attribute: true, type: String, default: "" },
    msId: { attribute: true, type: String, default: "" },
    msParentId: { attribute: true, type: String, default: "" },
    msDescription: { attribute: true, type: String, default: "" },
    msLeadName: { attribute: true, type: String, default: "" },
    msLeadEmail: { attribute: true, type: String, default: "" },
    msCatagory: { attribute: true, type: String, default: "" },
    msMajor: { attribute: true, type: Number, default: 0 },
    msMinor: { attribute: true, type: Number, default: 0 },
    msPatch: { attribute: true, type: Number, default: 0 },
    msDependencies: { attribute: true, type: Array, default: [] },
    msVersions: { attribute: true, type: Array, default: [] },
    msVersionsMenuItems: { attribute: true, type: Array, default: [] },
    msDependency:  { attribute: true, type: String, default: "" },
    msDependencyVersion:  { attribute: true, type: String, default: "" },
    searchName: { attribute: true, type: String, default: "" },
    searchCategory: { attribute: true, type: String, default: "" },
    isAll: { attribute: true, type: Boolean, default: false },
    depBackup: { attribute: true, type: Array, default: [] },
    catBackup: { attribute: true, type: Array, default: [] },
  }
})
export default class MainPage extends LitComponent {
  /**
   * Render the <e-main-page> component. This function is called each time a
   * prop changes.
   */
  
  _postMicroService() {
    
    const postData={
      name: this.shadowRoot.querySelector('eui-base-v0-text-field#msName').value,
      category: this.shadowRoot.querySelector('eui-base-v0-text-field#msCatagory').value,
      versionNumber: { 
        major: this.shadowRoot.querySelector('eui-base-v0-spinner#major').value,
        minor:  this.shadowRoot.querySelector('eui-base-v0-spinner#minor').value,
        patch:  this.shadowRoot.querySelector('eui-base-v0-spinner#patch').value},
      description: this.shadowRoot.querySelector('eui-base-v0-textarea#msDescription').value,
      leadEngineer: {
        name:  this.shadowRoot.querySelector('eui-base-v0-text-field#msLeadName').value,
        email: this.shadowRoot.querySelector('eui-base-v0-text-field#msLeadEmail').value
      },
      dependencies: this.getDependenciesBody()
      }
      console.log(JSON.stringify(postData))
    fetch("http://localhost:8080/microservices", {

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
      });}

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
    });}


    async _getMicroservicesByCategory(category) {
      await fetch("http://localhost:8080/microservices/category?category="+category)
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
     });}

     async _getMicroservicesByName(name) {
      await fetch("http://localhost:8080/microservices/"+name)
       .then((response) => response.json())
       .then((data) => {
         console.log("Success:", data);
         this.responseData = [data];
       })
       .catch((error) => {
         console.error("None Found:");
         alert(
           "No MicroServices of that name found"
         );
     });}

    async _getMicroservicesWithoutDependencies() {
      await fetch("http://localhost:8080/microservices/independent")
       .then((response) => response.json())
       .then((data) => {
         console.log("Success:", data);
         this.responseData = data;
       })
       .catch((error) => {
         console.error("None Found:");
         alert(
           "No MicroServices without dependencies found"
         );
       });
     }

    // TODO: Gearoid please create a searchbar for this
    searchByCategory = async (category) => {
      await this._getMicroservicesByCategory(category);
      this.executeRender();
    }

    searchByNoDependencies = async () => {
      await this._getMicroservicesWithoutDependencies();
      this.executeRender();
    }

    searchByName = async (name) => {
      await this._getMicroservicesByName(name);
      this.executeRender();
    }
    //and then call didconnect in a different button to reset search functionalities

    _getDependencyVersions(name) {
      fetch("http://localhost:8080/microservices/"+this.getId(name)+"/versions")
        .then((response) => response.json())
        .then((data) => {
          this.msVersions = data 
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

    async _getDependency(name,version) {
      await fetch("http://localhost:8080/microservices/"+this.getId(name)+"/versions/"+version)
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
  
  getDependenciesBody(){
    let dependencies= []
    this.msDependencies.forEach( async element => {
      dependencies.push(await this._getDependency(element.col1,element.col2))
    })
    console.log(dependencies)
    return dependencies
  }
  
  versionNumberBuild(element){
    let number = String(element.major)+'.'+String(element.minor)+'.'+String(element.patch)
    return number;
  }

  getId(name) {
    let id = name.toLowerCase().replace(/ /g, '_');
    return id
  }

  didConnect = async () => {
    await this._getMicroservices();
    this.isAll = true;
    this.executeRender();
  }; 

  getMenuItems(){
    let counter = 0
    this.msVersions.forEach(element => {
      counter+=1
      this.msVersionsMenuItems.push({"label":element,"value":'item-'+String(counter)})
    })
    console.log(this.msVersionsMenuItems)
  }

render() {
          // console.log(this.responseData)
          const columns = [
            { title: 'Microservice Name', attribute: 'col1' },
            { title: 'Category', attribute: 'col2' },
            { title: 'Latest Version', attribute: 'col3' },
            { title: 'Lead Engineer Name', attribute: 'col4' }
          ];
          let values = [];

          this.responseData.forEach(element => {
            values.push({col1: element.name,col2: element.category,col3: this.versionNumberBuild(element.currentVersion.versionNumber),col4: element.currentVersion.leadEngineer.name})
          });

          const main_table = html`
          <eui-table-v0 .columns=${columns} .data=${values} style="height: 300px" expanded-row-height="100px" striped single-select
            @eui-table:row-click=${(event) => {
                    this.showDetailContent = true;
                    this.msName = event.detail.col1;
                    this.showContent = false;
                    this.executeRender()
                    }}>
          </eui-table-v0>`

          let dependencies = [];
          let categories = [];
          if(this.isAll)
          {
            let categoriesSet = new Set();
            let counter = 0
            let counter1 = 0
            this.responseData.forEach(element => {
              counter+=1
              dependencies.push({"label":element.name,"value":'item-'+String(counter)})
              if(!categoriesSet.has(element.category))
              {
                categories.push({"label":element.category,"value":'item-'+String(counter1)})
                categoriesSet.add(element.category)
                counter1+=1
              }
            })
            this.catBackup = categories;
            this.depBackup = dependencies;
            this.isAll = false;
          }
          else
          {
            dependencies = this.depBackup;
            categories = this.catBackup;
          }
          
          

        

          const dependencyColumns = [
            { title: 'Dependency Name', attribute: 'col1' },
            { title: 'Dependency Version', attribute: 'col2' }
          ];

          
          

          const addMenu = html`
        <eui-base-v0-text-field fullwidth id='msName' name="msName" placeholder="Microservice Name"
          success-message="Valid Name">
        </eui-base-v0-text-field>
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
            event.target.data.forEach(element => {
              if(element.checked===true)
              {
                this.msDependency = element.label; 
                console.log(this.msDependency)
              
              }})
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

  if(this.showContent == true) {
    // this._getMicroservices()
    return html`
      
    <eui-layout-v0-multi-panel-tile maximize id="multi-panel-1" tile-title="Microservices">
      <div slot="content">
        <!-- Content for multi panel tile goes here -->
        ${main_table}
      </div>
    
      <eui-layout-v0-tile-panel id="search" tile-title="Search" slot="left" icon-name="search" width=300>
        <div slot="content">
          <!-- Content for "Filter" tile panel goes here -->
        </div>

        <div width="100%" slot="content">
          <div>
            <eui-base-v0-text-field id='searchName' name="searchName" placeholder="Name" primary></eui-base-v0-text-field>
          </div>
          <div>
            <eui-base-v0-button @click="${(event) => {
              let searchName = this.shadowRoot.querySelector('eui-base-v0-text-field#searchName').value
              this.searchByName(searchName)
              }}" 
              primary>Search Name
            </eui-base-v0-button>
          </div>
        </div>
        
        <br><br>

        <div width="100%" slot="content">
          <eui-base-v0-button @click="${(event) => {this.didConnect()}}" slot="content" primary>Show All MicroServices</eui-base-v0-button>
        </div>

        <br><br>

        <div width="100%" slot="content">
          <eui-base-v0-button @click="${(event) => {this.searchByNoDependencies()}}" 
          slot="content" primary>Show Independent MicroServices</eui-base-v0-button>
        </div>

        <br><br>
        
        <eui-base-v0-dropdown @eui-dropdown:click="${ (event) => {
          event.target.data.forEach(element => {
            if(element.checked===true)
            {
              console.log("this is there:" + element.checked);
              this.searchCategory = element.label; console.log(this.searchCategory)
            
            
            }
          })
          console.log(this.searchCategory)
          // this.getMenuItems()
        }}" 
        .data=${categories} slot="content" data-type="single" no-result-label="No Result Found" label="Category Name" width="20" >
        </eui-base-v0-dropdown>

        <eui-base-v0-button @click="${(event) => {
            this.searchByCategory(this.searchCategory)
          }}" 
          slot="content" primary>Search Category
        </eui-base-v0-button>
        
      </eui-layout-v0-tile-panel>
    
      <eui-layout-v0-tile-panel id="addMicroservice" tile-title="Add Microservice" slot="right" icon-name="plus" width=500>
        <div slot="content">
          ${addMenu}
        </div>
    
        <eui-base-v0-button slot="footer" @click=${(event) => {
            //this._testGet();
            this._postMicroService();
            this.didConnect()

          }} primary>Submit</eui-base-v0-button>
      </eui-layout-v0-tile-panel>
    </eui-layout-v0-multi-panel-tile>`;
    }
  if (this.showDetailContent == true) {
      return html`
      <e-ms-details-component msname=${this.msName}></e-ms-details-component>
    `
    }
  }
}

/**
 * Register the component as e-main-page.
 * Registration can be done at a later time and with a different name
 */
MainPage.register();
