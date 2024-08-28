import { Selector, ClientFunction } from 'testcafe';

/**
 * This Function is only used internally to traverse the Shadow DOM.
 * This function is used throughout the functions bellow
 * and should not be used outside of this file.
 * Starts at the currentElement in the DOM
 * and traverses the Shadow DOM to try and find the selectors.
 * If multiple components can be found on the same level in the DOM
 * with the same name then a number can be added to indicate which one is required.
 * @private
 * @param {Array | String} selectors - Required, can be an Array/String.
    Indicates which component to find.
    Array required if component is nested inside another component
 * @param {Object} currentElement - Required, dom element which indicates what part of the DOM
    to start searching for the element from.
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 */
const findElement = (selectors, currentElement, number) => {
  if (!Array.isArray(selectors)) {
    selectors = [selectors];
  }
  for (let i = 0; i < selectors.length; i += 1) {
    if (currentElement.querySelector(selectors[i]) == null) {
      currentElement = currentElement.shadowRoot;
    }
    if (currentElement) {
      if (number !== undefined && selectors.length - 1 === i) {
        const allElements = currentElement.querySelectorAll(selectors[i])[number];
        return allElements;
      }
      currentElement = currentElement.querySelector(selectors[i]);
    }
    if (!currentElement) {
      break;
    }
  }
  return currentElement;
};

const euiContainerShadowRoot = ClientFunction(componentName => document.querySelector('eui-container')
  .shadowRoot.querySelector(componentName).shadowRoot);

/**
 * Selector which finds an app in the menu panel. Native Testcafe actions can be performed on this.
 * @public
 * @param {String} selector - Required. Name of an app in the menu panel
 */
const findInMenu = Selector((selector) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const menu = componentLocation.querySelector('eui-app-nav').shadowRoot.querySelector('eui-menu-panel')
    .shadowRoot.querySelector('eui-navigation').shadowRoot.querySelectorAll('li');
  for (let x = 0; x < menu.length; x += 1) {
    const menuItem = menu[x];
    const appLink = menuItem.querySelector('a');
    const groupTitle = menuItem.querySelector('span');
    if (groupTitle.innerText === selector) {
      return groupTitle;
    }
    if (appLink.innerText === selector) {
      return appLink.parentNode;
    }
  }
  return null;
}, { dependencies: { euiContainerShadowRoot } });

/**
 * Function which expands/collapses an app in the menu bar
 * @public
 * @param {String} selector - Required. Name of an app in the menu panel to expand/collapse
 */
const expandCollapseAppInMenu = ClientFunction((selector) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const menu = componentLocation.querySelector('eui-app-nav').shadowRoot.querySelector('eui-menu-panel')
    .shadowRoot.querySelector('eui-navigation').shadowRoot.querySelectorAll('li');
  for (let x = 0; x < menu.length; x += 1) {
    const menuItem = menu[x];
    const appLink = menuItem.querySelector('a');
    if (appLink.innerText === selector) {
      return appLink.parentNode.querySelector('eui-base-v0-icon').click();
    }
  }
  return null;
}, { dependencies: { euiContainerShadowRoot } });

/**
 * Selector which finds a component inside an App.
 * The actionInApp function is needed to perform actions on these components
 * @public
 * @param {Array | String} selectors - Required, can be an Array/String.
    Indicates which component to find.
    Array required if component is nested inside another component
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 */
const findInApp = Selector((selectors, number) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const currentElement = componentLocation.querySelector('eui-app-content').shadowRoot;
  return findElement(selectors, currentElement, number);
}, { dependencies: { findElement, euiContainerShadowRoot } });

/**
 * Selector which finds a component inside the SystemBar.
 * Native Testcafe actions can be performed on this.
 * @public
 * @param {Array | String} selectors - Required, can be an Array/String.
    Indicates which component to find.
    Array required if component is nested inside another component
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 */
const findInSystemBar = Selector((selectors, number) => {
  const currentElement = euiContainerShadowRoot('eui-container-system-bar');
  return findElement(selectors, currentElement, number);
}, { dependencies: { findElement, euiContainerShadowRoot } });

/**
 * Selector which finds a component inside the SystemPanel.
 * Native Testcafe actions can be performed on this.
 * @public
 * @param {Array | String} selectors - Required, can be an Array/String.
    Indicates which component to find.
    Array required if component is nested inside another component
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 */
const findInSystemPanel = Selector((selectors, number) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const currentElement = componentLocation.querySelector('eui-app-bar').shadowRoot;
  return findElement(selectors, currentElement, number);
}, { dependencies: { findElement, euiContainerShadowRoot } });

/**
 * Selector which finds a component inside the AppBar.
 * Native Testcafe actions can be performed on this.
 * @public
 * @param {Array | String} selectors - Required, can be an Array/String.
    Indicates which component to find.
    Array required if component is nested inside another component
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 */
const findInAppBar = Selector((selectors, number) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const currentElement = componentLocation.querySelector('eui-app-bar').shadowRoot;
  return findElement(selectors, currentElement, number);
}, { dependencies: { findElement, euiContainerShadowRoot } });

/**
 * Function which performs an Action on a component inside an App
 * @public
 * @param {Array | String} selectors - Required, can be an Array/String.
    Indicates which component to find.
    Array required if component is nested inside another component
 * @param {String} action - Required. Indicates the action to perform on a component.
    Can be 'click'/'typeText'/'scroll'
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 * @param {String} inputValue - Required for action = 'typeText'. Text to be inputted
 */
const actionInApp = ClientFunction((selectors, action, number, inputValue) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const currentElement = componentLocation.querySelector('eui-app-content').shadowRoot;
  const element = findElement(selectors, currentElement, number);
  if (action === 'click') {
    element.click();
  }
  if (action === 'typeText') {
    element.setAttribute('value', inputValue);
    element.dispatchEvent(new Event('input', {
      bubbles: true,
      cancelable: true,
    }));
  }
  if (action === 'scroll') {
    element.scrollIntoView();
  }
}, { dependencies: { findElement, euiContainerShadowRoot } });

/**
 * Function which performs an Action on a dropdown component in an app.
 * @public
 * @param {Array} selectors - Required. Array must include the dropdown element to click on
 * @param {String} action - Required. Indicates the action to perform on a component.
    Can be 'click'/'selectAll'/'getSelected'
 * @param {Number} number - Optional, provided if multiple elements exist.
    If not provided takes the first element
 * @param {Number} inputValue - If used with action = 'click' clicks
    on the inputted number inside the dropdown
 */
const actionDropdownInApp = ClientFunction((selectors, action, number, inputValue) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const currentElement = componentLocation.querySelector('eui-app-content').shadowRoot;
  const element = findElement(selectors, currentElement, number);
  if (action === 'click') {
    if (!inputValue) {
      inputValue = 0;
    }
    const dropdown = element.shadowRoot.querySelector('eui-base-v0-button');
    const menuItem = element.shadowRoot.querySelectorAll('eui-base-v0-menu-item')[inputValue];
    dropdown.click();
    menuItem.click();
  }
  if (action === 'selectAll') {
    const dropdown = element.shadowRoot.querySelector('eui-base-v0-button');
    const menuItem = element.shadowRoot.querySelector('eui-base-v0-menu').shadowRoot
      .querySelector('eui-base-v0-menu-item#select-all');
    dropdown.click();
    menuItem.click();
  }
}, { dependencies: { findElement, euiContainerShadowRoot } });

/**
* Selector which return a list of selected menu items from a dropdown as a node list
* @public
* @param {Array} selectors - Required. Dropdown to get the selected items of
* @param {Number} number - Optional, provided if multiple elements exist.
If not provided takes the first element
*/
const getSelectedDropdownInApp = Selector((selectors, number) => {
  const componentLocation = euiContainerShadowRoot('eui-container-layout-holder');
  const currentElement = componentLocation.querySelector('eui-app-content').shadowRoot;
  const element = findElement(selectors, currentElement, number);
  const menuItems = element.shadowRoot.querySelectorAll('eui-base-v0-menu-item');
  let selected = [];
  for (let i = 0; i < menuItems.length; i += 1) {
    if (menuItems[i].hasAttribute('selected')) {
      selected.push(menuItems[i]);
    }
  }
  return selected;
}, { dependencies: { findElement, euiContainerShadowRoot } });

export {
  findInMenu,
  expandCollapseAppInMenu,
  findInApp,
  actionInApp,
  findInSystemBar,
  findInSystemPanel,
  findInAppBar,
  actionDropdownInApp,
  getSelectedDropdownInApp,
};
