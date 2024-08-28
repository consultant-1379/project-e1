/**
 * Component DependencyGraphComponent is defined as
 * `<e-dependency-graph-component>`
 *
 * Imperatively create component
 * @example
 * let component = new DependencyGraphComponent();
 *
 * Declaratively create component
 * @example
 * <e-dependency-graph-component></e-dependency-graph-component>
 *
 * @extends {LitComponent}
 */
import { definition } from '@eui/component';
import { LitComponent, html } from '@eui/lit-component';
import style from './dependencyGraphComponent.css';
import file from './j.json'
import * as d3 from 'd3';
// import findAllDeep from './util.js';
// // import * as vega from 'vega-em';
// import embed from 'vega-embed';
// import VegaEmbed from './vegaembed'


/**
 * @property {Boolean} propOne - show active/inactive state.
 * @property {String} propTwo - shows the "Hello World" string.
 */
@definition('e-dependency-graph-component', {
  style,
  home: 'dependency-graph-component',
  props: {
    propOne: { attribute: true, type: Boolean },
    propTwo: { attribute: true, type: String, default: 'Hello World' },
  },
})


export default class DependencyGraphComponent extends LitComponent {
  /**
   * Render the <e-dependency-graph-component> component. This function is called each time a
   * prop changes.
   */
  constructor() {
    super();

  }


  // vegaFunction() {
  //   const spec = file;
  //   embed("#vis", spec)
  //     .then(result => console.log(result))
  //     .catch(console.warn);
  // }




  doGraph() {

    const PADDING_LABEL = 30
    // Some constants controlling the graph appearance
    var width = 460
    var height = 460

    const body = document.querySelector('body')
    const euiContainer = body.querySelector('eui-container')

    function findAllDeep(parent, selectors, depth = null) {
      let nodes = new Set();
      let currentDepth = 1
        ;
      const recordResult = (nodesArray) => {
        for (const node of nodesArray) {
          nodes.add(node)
        }
      }
      const recursiveSeek = _parent => {
        // check for selectors in lightdom
        recordResult(_parent.querySelectorAll(selectors));
        if (_parent.shadowRoot) {
          // check for selectors in shadowRoot
          recordResult(_parent.shadowRoot.querySelectorAll(selectors));
          // look for nested components with shadowRoots
          for (let child of [..._parent.shadowRoot.querySelectorAll('*')].filter(i => i.shadowRoot)) {
            // make sure we haven't hit our depth limit
            if (depth === null || currentDepth < depth) {
              recursiveSeek(child);
            }
          }
        }
      };
      recursiveSeek(parent);
      const [first] = nodes
      return first;
    };

    var local = findAllDeep(euiContainer, `[id="my_dataviz"]`, 20); //getShadowRoot(eDependencyGraphComponent, "#my_dataviz")
    console.log(local)

    var svg = d3.select(local)
      .append("svg")
      .attr("width", width+100)
      .attr("height", height+100)
      .append("g")
      .attr("transform", "translate(10,0)");  // bit of margin on the left = 40


    const jsonData = file;
    // Scale for the bubble size
    function call(data) {
      // Create the cluster layout:
      var cluster = d3.cluster()
        .size([height, width - 100]);  // 100 is the margin I will have on the right side

      // Give the data to this cluster layout:
      var root = d3.hierarchy(data, function (d) {
        return d.children;
      });
      cluster(root);


      // Add the links between nodes:
      svg.selectAll('path')
        .data(root.descendants().slice(1))
        .enter()
        .append('path')
        .attr("d", function (d) {
          return "M" + d.y + "," + d.x
            + "C" + (d.parent.y + 50) + "," + d.x
            + " " + (d.parent.y + 150) + "," + d.parent.x // 50 and 150 are coordinates of inflexion, play with it to change links shape
            + " " + d.parent.y + "," + d.parent.x;
        })
        .style("fill", 'none')
        .attr("stroke", '#ccc')
      // root.descendants().map(function(e){ e.value.})
      console.log(root.descendants())
      let counter = 0
      // Add a circle for each node.
      svg.selectAll("g")
        .data(root.descendants())
        .enter()
        .append("g")
        .attr("transform", function (d) {
          return "translate(" + d.y + "," + d.x + ")"
        })
        .append("circle")
        .attr("class", "node")
        .attr("id", function (n) { return n.data.name; })
        .attr("r", 7)
        .style("fill", "#69b3a2")
        .attr("stroke", "black")
        .style("stroke-width", 2)

      const level2 = root.descendants().slice(1, 3)

      console.log(level2)


      var text = svg.append("g")
        .attr("class", "nodes")
        .selectAll("g")
        .data(root.descendants())
        .enter()
        .append("g")
        .attr("transform", function (d) {
          console.log(d)
          return "translate(" + d.y + "," + d.x + ")"
        }).append("text")
        .attr("id", function (n) { return n.data.name+"-text"; })
        .attr("dx", 12)
        .attr("dy", ".35em")
        .text(function (d) { return d.data.name; });

      // Add a circle for each node. 
      var levelDepndecies = findAllDeep(euiContainer, `[id="Dependcies"]`, 20);
      var levelDepndeciesText = findAllDeep(euiContainer, `[id="Dependcies-text"]`, 20);
      d3.select(levelDepndecies).style("opacity", 0)
      d3.select(levelDepndeciesText).style("font-weight",900)


      var levelDependents = findAllDeep(euiContainer, `[id="Dependents"]`, 20);
      var levelDependentsText = findAllDeep(euiContainer, `[id="Dependents-text"]`, 20);
      d3.select(levelDependents).style("opacity", 0)
      d3.select(levelDependentsText).style("font-weight",900)
    }
    call(jsonData)
    // text hover nodes
  }






  render() {
    return html`
      <eui-base-v0-button @click=${this.doGraph}>Click</eui-base-v0-button>
      <!-- <vega-embed></vega-embed> -->
      <div slot="content" id="my_dataviz"></div>
      <!-- <eui-chart-v0-vega spec-url="https://vega.github.io/vega/examples/edge-bundling.vg.json"></eui-chart-v0-vega> -->
    `;
  }
}

/**
 * Register the component as e-dependency-graph-component.
 * Registration can be done at a later time and with a different name
 */
DependencyGraphComponent.register();
