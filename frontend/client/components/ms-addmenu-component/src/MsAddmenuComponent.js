/**
 * Component MsAddmenuComponent is defined as
 * `<e-ms-addmenu-component>`
 *
 * Imperatively create component
 * @example
 * let component = new MsAddmenuComponent();
 *
 * Declaratively create component
 * @example
 * <e-ms-addmenu-component></e-ms-addmenu-component>
 *
 * @extends {LitComponent}
 */
import { definition } from '@eui/component';
import { LitComponent, html } from '@eui/lit-component';
import style from './msAddmenuComponent.css';

/**
 * @property {Boolean} propOne - show active/inactive state.
 * @property {String} propTwo - shows the "Hello World" string.
 */
@definition('e-ms-addmenu-component', {
  style,
  home: 'ms-addmenu-component',
  props: {
    propOne: { attribute: true, type: Boolean },
    propTwo: { attribute: true, type: String, default: 'Hello World' },
  },
})
export default class MsAddmenuComponent extends LitComponent {
  /**
   * Render the <e-ms-addmenu-component> component. This function is called each time a
   * prop changes.
   */
  render() {

    const dependencyColumns = [
      { title: 'Dependency Name', attribute: 'col1' }
    ];

    return html`
    <eui-base-v0-text-field fullwidth name="msName" placeholder="Microservice Name" success-message="Valid Name"></eui-base-v0-text-field>
    <eui-base-v0-textarea fullwidth name="item" cols="20" placeholder="Description"></eui-base-v0-textarea>
    <eui-base-v0-text-field fullwidth name="msLeadName" placeholder="Lead Engineer Name" success-message="Valid Name"></eui-base-v0-text-field>
    <eui-base-v0-text-field fullwidth name="msLeadEmail" placeholder="Lead Engineer Email" success-message="Valid Email"></eui-base-v0-text-field>
    <eui-base-v0-text-field size="41" name="msCatagory" placeholder="Catagory"></eui-base-v0-text-field>
    <eui-base-v0-info-popup message="Eg. Database, Messaging, Application" position="bottom-end"></eui-base-v0-info-popup>
    <eui-base-v0-text-field size="41" name="msVersion" placeholder="Version"></eui-base-v0-text-field>
    <eui-base-v0-info-popup message="Must be standard Semantic Versioning." position="bottom-end"></eui-base-v0-info-popup>
    <eui-base-v0-text-field size="35" name="dependency" placeholder="Dependency"></eui-base-v0-text-field>
    <eui-base-v0-button id="add-button" icon="plus" --btn-font-size></eui-base-v0-button>
    <eui-table-v0 tiny .columns=${dependencyColumns}></eui-table-v0>
    `;
  }
}

/**
 * Register the component as e-ms-addmenu-component.
 * Registration can be done at a later time and with a different name
 */
MsAddmenuComponent.register();
