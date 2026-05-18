# Navigation Routes

Routes used by `NavigationAgent` to reach Helix IPLM pages via UI actions
instead of direct URL navigation.

Each route has a **hash pattern**, a **description**, and an ordered list of
**click steps**.  Every step has a selector type (`css` or `xpath`) and a
human-readable label.

Mark a route `[dynamic]` when the URL contains variable segments (FQN / ID)
that cannot be reached through a fixed menu path — the agent will fall back to
`page.goto()` for those.

Source: analysis of `phi_web_automation` repo  
 - `HeaderPage.java` → top nav-bar CSS/XPath selectors  
 - `HeaderSubMenuPage.java` → dropdown submenu CSS selectors  
 - `CommonUrls.java` → all hash-based URL routes

---

## dashboard
> Return to Dashboard — click the product logo

1. css: `.app-header__navbar__brand a.navbar-brand` — Click product logo

---

## home
> Return to Home — click the product logo

1. css: `.app-header__navbar__brand a.navbar-brand` — Click product logo

---

## ip/catalog
> IP Catalog — Catalogs ▸ IP catalog

1. css: `css: div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. css: `xpath: //li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog'] — Click IP Catalog item` — 

---

## library/catalog
> Library Catalog — Catalogs ▸ Library catalog

1. css: `div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. xpath: `//li[contains(@class,'library-catalog')]//span[normalize-space()='Library catalog']` — Click Library Catalog item

---

## query/search
> Advanced Search — Catalogs ▸ Advanced search

1. css: `div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. xpath: `//a[@data-testid='menu-item-link']//span[text()='Advanced search']` — Click Advanced search item

---

## shoppingcart
> Shopping Cart — click the Shopping Cart icon in the header

1. css: `li[data-testid='shopping-cart'] > a` — Click Shopping Cart icon

---

## ip/create
> Create new IP — Create ▸ New IP

1. css: `div[data-testid='ui-dropdown-menu-create'] > button` — Open Create dropdown
2. css: `.menu-new__create-ip` — Click New IP option

---

## library/manage
> Library Management — Administration ▸ Libraries

1. css: `div[data-testid='ui-dropdown-menu-admin'] > button` — Open Administration dropdown
2. css: `.menu-admin__library-manage a[data-testid='menu-item-link']` — Click Libraries link

---

## labels/manage
> Labels Management — Administration ▸ Labels

1. css: `div[data-testid='ui-dropdown-menu-admin'] > button` — Open Administration dropdown
2. css: `.menu-admin__labels-manage a[data-testid='menu-item-link']` — Click Labels link

---

## property/manage
> Property Management — Administration ▸ Properties

1. css: `div[data-testid='ui-dropdown-menu-admin'] > button` — Open Administration dropdown
2. css: `.menu-admin__property-manage a[data-testid='menu-item-link']` — Click Properties link

---

## propertysets/manage
> Property Sets — Administration ▸ Property Sets

1. css: `div[data-testid='ui-dropdown-menu-admin'] > button` — Open Administration dropdown
2. css: `.menu-admin__propertysets a[data-testid='menu-item-link']` — Click Property Sets link

---

## queryfolder/manage
> Query Folders — Administration ▸ Query Folders

1. css: `div[data-testid='ui-dropdown-menu-admin'] > button` — Open Administration dropdown
2. css: `.menu-admin__queryfolder-manage a[data-testid='menu-item-link']` — Click Query Folders link

---

## geofencing/manage
> Geofencing — Administration ▸ Geofencing

1. css: `div[data-testid='ui-dropdown-menu-admin'] > button` — Open Administration dropdown
2. css: `.menu-admin__geofencing-manage a[data-testid='menu-item-link']` — Click Geofencing link

---

## ip/ [dynamic]
> IP detail / version / edit page — FQN or ID embedded in URL, no menu entry

---

## ipv/ [dynamic]
> IP Version page — version ID embedded in URL, no menu entry

---

## library/ [dynamic]
> Library detail page — library ID embedded in URL, no menu entry

---

## labels/ [dynamic]
> Label detail page — label ID embedded in URL, no menu entry

---

## property/manage/ [dynamic]
> Property detail page — property ID embedded in URL, no menu entry

---

## propertysets/manage/ [dynamic]
> Property Set detail page — property set ID embedded in URL, no menu entry

---

## queryfolder/manage/ [dynamic]
> Query Folder detail page — folder ID embedded in URL, no menu entry

---

## custom/ [dynamic]
> Custom page (bar / foo / lineage) — no menu entry

## abc/ [dynamic]
> abc page — variable segment in URL, no fixed menu path

---

## ip/arm.cortex2/details
> IP › ARM.cortex2 details — Catalogs ▸ IP catalog ▸ click ARM.cortex2 row

1. css: `div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. xpath: `//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']` — Click IP catalog item
3. xpath: `//div[@col-id='name']//*[@title='ARM.cortex2']` — Click ARM.cortex2 row in IP catalog table

---

## ip/arm.cortex/details
> IP › ARM.cortex details — Catalogs ▸ IP catalog ▸ click ARM.cortex row

1. css: `div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. xpath: `//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']` — Click IP catalog item
3. xpath: `//div[@col-id='name']//*[@title='ARM.cortex']` — Click ARM.cortex row in IP catalog table

---

## ip/arm.cortex/usage
> IP › ARM.cortex usage — Catalogs ▸ IP catalog ▸ click ARM.cortex row ▸ Usage tab

1. css: `div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. xpath: `//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']` — Click IP catalog item
3. xpath: `//div[@col-id='name']//*[@title='ARM.cortex']` — Click ARM.cortex row in IP catalog table
4. text: `Usage` — Click Usage tab

---

## ip/arm.cortex/dashboard
> ip › arm.cortex › dashboard page — Catalogs ▸ IP catalog ▸ click ARM.cortex row ▸ Dashboard tab

1. css: `div[data-testid='ui-dropdown-menu-catalog'] > button` — Open Catalogs dropdown
2. xpath: `//li[contains(@class,'ip-catalog')]//span[normalize-space()='IP catalog']` — Click IP catalog item
3. xpath: `//div[@col-id='name']//*[@title='ARM.cortex']` — Click ARM.cortex row in IP catalog table
4. text: `Dashboard` — Click Dashboard tab

---

## ip/arm.cortex2/dashboard
> ip › arm.cortex2 › dashboard page

1. text: `Open IPcatalog page` — Open IPcatalog page
2. text: `Ipcatalog page, In table of Ip name column, cortex2 is displaying in Ip name column so click on it cortex2` — Ipcatalog page, In table of Ip name column, cortex2 is displaying in Ip name column so click on it cortex2
3. text: `Cortex2 IP details page open then click to dashboard page` — Cortex2 IP details page open then click to dashboard page

---

## ip/arm.parent/usage
> ip › arm.parent › usage page

1. text: `Open IPcatalog page` — Open IPcatalog page
2. text: `search cortex in Ipcatalog page` — search cortex in Ipcatalog page
3. text: `cortex ip displaying in table` — cortex ip displaying in table
4. text: `click to cortex link in ip name column in table` — click to cortex link in ip name column in table
5. text: `Cortex IP details page open then click to usage page` — Cortex IP details page open then click to usage page

---
