package com.aisystems.firefliescrmautomation;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swagger-ui")
public class SwaggerUiController {

    @GetMapping(value = {"/index.html", "/"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> index() {
        String html = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\"/>\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>\n" +
                "  <title>AI SYSTEMS API</title>\n" +
                "  <!-- Favicon: prefer local, but fall back to a public CDN icon if not present -->\n" +
                "  <link rel=\"icon\" type=\"image/x-icon\" href=\"/AISYSTEMS/favicon.ico\" />\n" +
                "  <link rel=\"icon\" type=\"image/png\" href=\"/AISYSTEMS/favicon.png\" sizes=\"32x32\" />\n" +
                "  <!-- Force a specific remote favicon to be used for the custom Swagger UI -->\n" +
                "  <script>\n" +
                "    (function(){\n" +
                "      const faviconUrl = 'https://unpkg.com/swagger-ui-dist@4.19.1/favicon-32x32.png';\n" +
                "      function setFavicon(url){\n" +
                "        try{\n" +
                "          // remove existing icon links so the browser picks up the new one\n" +
                "          Array.from(document.querySelectorAll('link[rel~=\"icon\"], link[rel~=\"shortcut icon\"]')).forEach(n=>n.parentNode && n.parentNode.removeChild(n));\n" +
                "          var l = document.createElement('link'); l.rel='icon'; l.type='image/png'; l.href = url; document.head.appendChild(l);\n" +
                "          var s = document.createElement('link'); s.rel='shortcut icon'; s.href = url; document.head.appendChild(s);\n" +
                "          console.info('[swagger-ui] favicon set to', url);\n" +
                "        }catch(e){ console.warn('[swagger-ui] setFavicon error', e); }\n" +
                "      }\n" +
                "      // apply shortly after load\n" +
                "      setTimeout(()=>setFavicon(faviconUrl), 100);\n" +
                "    })();\n" +
                "  </script>\n" +
                "  <link rel=\"stylesheet\" href=\"/webjars/swagger-ui/swagger-ui.css\" />\n" +
                "  <!-- CDN fallback (also load to ensure styles apply) -->\n" +
                "  <link rel=\"stylesheet\" href=\"https://unpkg.com/swagger-ui-dist@4/swagger-ui.css\" />\n" +
                "  <!-- Minimal inline fallback styles to ensure the UI is readable if full CSS fails -->\n" +
                "  <style>\n" +
                "    :root{--accent:#1572e8;--muted:#69707a;--card-bg:#ffffff;--glass:#f6f8fb}\n" +
                "    body{font-family:Inter, Arial, Helvetica, sans-serif; margin:0; padding:0; color:#1f2937; background:linear-gradient(180deg,#fbfdff, #f7f9fc)}\n" +
                "    #swagger-ui{padding:18px;}\n" +
                "    .opblock{border:1px solid #e6edf5;padding:8px;margin:8px 0;background:#fff;border-radius:6px}\n" +
                "    .info{margin-bottom:12px;}\n" +
                "    .scheme-container {margin-bottom:8px;}\n" +
                "\n" +
                "    /* Page header (API meta) */\n" +
                "    #api-meta.api-meta{display:flex;gap:16px;align-items:center;padding:16px;border-radius:10px;background:linear-gradient(90deg, rgba(21,114,232,0.06), rgba(14,165,233,0.03));box-shadow:0 6px 18px rgba(31,41,55,0.04);margin-bottom:12px}\n" +
                "    .api-logo{width:64px;height:64px;border-radius:10px;background-size:48px 48px;background-position:center center;background-repeat:no-repeat;background-image:url('https://unpkg.com/swagger-ui-dist@4.19.1/favicon-32x32.png');flex:0 0 64px;box-shadow:0 4px 12px rgba(21,114,232,0.08)}\n" +
                "    .api-info{flex:1;min-width:0}\n" +
                "    .api-title{font-size:18px;margin:0 0 4px 0;color:var(--accent);font-weight:700}\n" +
                "    .api-desc{margin:0;color:var(--muted);font-size:13px}\n" +
                "    .api-stats{margin-top:8px;display:flex;gap:10px;align-items:center}\n" +
                "    .api-badge{background:var(--card-bg);padding:6px 10px;border-radius:999px;border:1px solid #e6eefc;color:var(--muted);font-weight:600;font-size:13px}\n" +
                "\n" +
                "    /* Controllers table styling */\n" +
                "    .controller-container{margin-top:8px;background:var(--card-bg);padding:12px;border-radius:10px;border:1px solid #e9f0fa;box-shadow:0 4px 12px rgba(15,23,42,0.03)}\n" +
                "    .controller-search{margin-bottom:8px;display:flex;justify-content:space-between;align-items:center;gap:12px}\n" +
                "    .controller-search input{width:100%;max-width:420px;padding:8px 10px;border:1px solid #e0e6ef;border-radius:8px;font-size:14px}\n" +
                "    .controller-table{width:100%;border-collapse:collapse;font-size:16px;background:transparent;margin-top:8px}\n" +
                "    .controller-table thead th{position:sticky;top:0;background:transparent;border-bottom:1px solid #e6eef8;padding:10px 12px;text-align:left;z-index:2;font-weight:600;color:#0f1724}\n" +
                "    .controller-table tbody td{padding:10px 12px;border-bottom:1px solid #f4f6fb;color:#334155}\n" +
                "    .controller-table tbody tr:nth-child(odd){background:linear-gradient(90deg,#ffffff,#fbfdff)}\n" +
                "    .controller-table tbody tr:hover{background:#eef8ff}\n" +
                "    .controller-table .col-index{width:6%;white-space:nowrap;font-weight:700;color:#0f1724}\n" +
                "    .controller-table .col-ops{width:12%;text-align:right;white-space:nowrap;color:#0f1724}\n" +
                "    .controller-empty{padding:10px;color:#6b7280;font-style:italic}\n" +
                "    .controller-actions{margin-bottom:6px}\n" +
                "    .small-muted{color:#6b7280;font-size:13px}\n" +
                "  </style>\n" +
                "  <script>\n" +
                "    // Check whether swagger-ui CSS is actually loaded; if not, load CDN fallback after a short delay\n" +
                "    (function(){\n" +
                "      function hasSwaggerCss(){\n" +
                "        try{\n" +
                "          for(var i=0;i<document.styleSheets.length;i++){\n" +
                "            var href=document.styleSheets[i].href;\n" +
                "            if(href && href.indexOf('swagger-ui')!==-1) return true;\n" +
                "          }\n" +
                "        }catch(e){}\n" +
                "        return false;\n" +
                "      }\n" +
                "      setTimeout(function(){ if(!hasSwaggerCss()){ var l=document.createElement('link'); l.rel='stylesheet'; l.href='https://unpkg.com/swagger-ui-dist@4/swagger-ui.css'; document.head.appendChild(l); console.info('[swagger-ui] loaded CDN CSS fallback'); } }, 600);\n" +
                "    })();\n" +
                "  </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"api-meta\" class=\"api-meta\">\n" +
                "  <div class=\"api-logo\" aria-hidden=\"true\"></div>\n" +
                "  <div class=\"api-info\">\n" +
                "    <h2 class=\"api-title\">Swagger UI for AI SYSTEMS HEALTH API</h2>\n" +
                "    <p id=\"api-description\" class=\"api-desc\">Description: interactive API documentation generated from the OpenAPI spec.  Manuela Cortés Granados</p>\n" +
                "    <div class=\"api-stats\">\n" +
                "      <div class=\"api-badge\" id=\"controllers-summary\">Controllers: <span id=\"controllers-count\">0</span></div>\n" +
                "      <div class=\"small-muted\">Operations are grouped by controller tags below</div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "<div id=\"controller-list-container\"></div>\n" +
                "<div id=\"swagger-ui\"></div>\n" +
                "<div id=\"swagger-error\" style=\"position:fixed;bottom:0;left:0;right:0;background:#fee;border-top:1px solid #f00;padding:8px;color:#900;display:none;z-index:99999;\"></div>\n" +
                "<script>\n" +
                "(function(){\n" +
                "  function showError(msg){ try{ var el=document.getElementById('swagger-error'); el.style.display='block'; el.innerText=msg; }catch(e){ console.error('showError', e); } }\n" +
                "  function loadScript(src){ return new Promise(function(res, rej){ var s=document.createElement('script'); s.src=src; s.onload=function(){res(src)}; s.onerror=function(){rej(src)}; document.head.appendChild(s); }); }\n" +
                "  async function fetchAndRenderControllers(){\n" +
                "    try{\n" +
                "      const res = await fetch('/v3/api-docs');\n" +
                "      if(!res.ok) return;\n" +
                "      const j = await res.json();\n" +
                "      let tags = Array.isArray(j.tags) ? j.tags.map(t=>t.name).filter(Boolean) : [];\n" +
                "      if(tags.length===0 && j.paths){\n" +
                "        // derive tags from operations if top-level tags missing\n" +
                "        const set = new Set();\n" +
                "        Object.values(j.paths).forEach(pi=>{\n" +
                "          try{ Object.values(pi).forEach(op=>{ if(op && Array.isArray(op.tags)) op.tags.forEach(tt=>set.add(tt)); }); }catch(e){}\n" +
                "        });\n" +
                "        tags = Array.from(set);\n" +
                "      }\n" +
                "      tags.sort((a,b)=>a.localeCompare(b, undefined, {sensitivity:'base'}));\n" +
                "      // build counts per tag by scanning operations\n" +
                "      const counts = {};\n" +
                "      if (j.paths) {\n" +
                "        try {\n" +
                "          Object.values(j.paths).forEach(pi => {\n" +
                "            try {\n" +
                "              Object.values(pi).forEach(op => {\n" +
                "                if (!op) return;\n" +
                "                if (Array.isArray(op.tags) && op.tags.length) {\n" +
                "                  op.tags.forEach(tt => { if (tt) counts[tt] = (counts[tt] || 0) + 1; });\n" +
                "                } else {\n" +
                "                  counts['Untagged'] = (counts['Untagged'] || 0) + 1;\n" +
                "                }\n" +
                "              });\n" +
                "            } catch (ex) { /* ignore per-operation errors */ }\n" +
                "          });\n" +
                "        } catch (ex) { /* ignore */ }\n" +
                "      }\n" +
                "      // update header controllers count badge (use tags.length for total tags)\n" +
                "      try{ const cc = document.getElementById('controllers-count'); if(cc) cc.textContent = String(tags.length || 0); }catch(e){}\n" +
                "      // support both old and new container ids\n" +
                "      const ul = document.getElementById('controller-list') || document.getElementById('controller-list-container');\n" +
                "      function scrollToTag(tagName){\n" +
                "         const safe = s => (s||'').replace(/\\s+/g,' ').trim();\n" +
                "         const norm = s => safe(s).toLowerCase();\n" +
                " \n" +
                "         const slug = s => safe(s).toLowerCase().replace(/[^a-z0-9]+/g,'-').replace(/(^-|-$)/g,'');\n" +
                " \n" +
                "        // Quick check: if a tag map exists (built by MutationObserver), use it first\n" +
                "        try{\n" +
                "          if(window && window._swaggerTagMap){\n" +
                "            const key = norm(tagName);\n" +
                "            const direct = window._swaggerTagMap[key] || window._swaggerTagMap[tagName] || window._swaggerTagMap[slug(tagName)];\n" +
                "            if(direct){ console.info('[swagger-custom] matched via tagMap for', tagName); scrollAndOpen(direct); return; }\n" +
                "          }\n" +
                "        }catch(e){ /* ignore */ }\n" +
                " \n" +
                "         const tryFindAndScroll = () => {\n" +
                "           try {\n" +
                "             // 1) Try id-based match used by some Swagger UI versions: operations-tag-<slug>\n" +
                "             const idCandidate = 'operations-tag-' + slug(tagName);\n" +
                "             const byId = document.getElementById(idCandidate);\n" +
                "             if(byId){ console.info('[swagger-custom] matched by id', idCandidate); scrollAndOpen(byId); return true; }\n" +
                " \n" +
                "             // 2) data-tag attribute (some UIs set data-tag on sections)\n" +
                "             const byDataExact = document.querySelector('[data-tag=\"'+tagName+'\"], [data-tag=\"'+slug(tagName)+'\"]');\n" +
                "             if(byDataExact){ console.info('[swagger-custom] matched by data-tag'); scrollAndOpen(byDataExact); return true; }\n" +
                " \n" +
                "             // 3) Try common selectors and exact text match (normalized)\n" +
                "             const selectors = ['.opblock-tag', '.opblock-tag__name', '.opblock-summary', '.opblock .opblock-summary', '.tag', '.resource', '.section-title', '.opblock-title', '.swagger-ui .opblock-tag'];\n" +
                "             for(const sel of selectors){\n" +
                "               const els = Array.from(document.querySelectorAll(sel));\n" +
                "               for(const el of els){\n" +
                "                 const text = norm(el.textContent || '');\n" +
                "                 if(text && text === norm(tagName)){\n" +
                "                   console.info('[swagger-custom] matched by selector', sel, 'text=', el.textContent.trim());\n" +
                "                   scrollAndOpen(el);\n" +
                "                   return true;\n" +
                "                 }\n" +
                "               }\n" +
                "             }\n" +
                " \n" +
                "             // 4) fallback: find elements whose id contains slug\n" +
                "             const allWithId = Array.from(document.querySelectorAll('[id]'));\n" +
                "             for(const el of allWithId){\n" +
                "               if(el.id && el.id.toLowerCase().indexOf(slug(tagName)) !== -1){\n" +
                "                 console.info('[swagger-custom] matched by id-contains', el.id);\n" +
                "                 scrollAndOpen(el);\n" +
                "                 return true;\n" +
                "               }\n" +
                "             }\n" +
                " \n" +
                "             // 5) last resort: scan visible text nodes under #swagger-ui\n" +
                "             const root = document.getElementById('swagger-ui') || document.body;\n" +
                "             const nodes = Array.from(root.querySelectorAll('*'));\n" +
                "             for(const el of nodes){\n" +
                "               if(el.children && el.children.length>0) continue; // prefer leaf nodes\n" +
                "               const text = norm(el.textContent || '');\n" +
                "               if(text && text === norm(tagName)){\n" +
                "                 console.info('[swagger-custom] matched by text-node');\n" +
                "                 scrollAndOpen(el);\n" +
                "                 return true;\n" +
                "               }\n" +
                "             }\n" +
                "           } catch(e){ console.warn('[swagger-custom] tryFindAndScroll error', e); }\n" +
                "           return false;\n" +
                "         };\n" +
                " \n" +
                "         function scrollAndOpen(el){\n" +
                "           const section = el.closest('.opblock-tag-section') || el.closest('.opblock') || el.closest('[data-tag]') || el;\n" +
                "           try{ section && section.scrollIntoView({behavior:'smooth', block:'start'}); }catch(e){}\n" +
                "           // highlight briefly\n" +
                "           try{\n" +
                "             const old = section.style.boxShadow;\n" +
                "             section.style.transition = 'box-shadow 0.25s ease, background 0.25s ease';\n" +
                "             section.style.boxShadow = '0 0 0 3px rgba(0,123,255,0.2)';\n" +
                "             setTimeout(()=>{ section.style.boxShadow = old || ''; }, 1800);\n" +
                "           }catch(e){}\n" +
                "           // click candidate header elements to expand\n" +
                "           try{\n" +
                "             const candidates = [];\n" +
                "             candidates.push(section.querySelector('.opblock-summary'));\n" +
                "             candidates.push(section.querySelector('.opblock-tag'));\n" +
                "             candidates.push(section.querySelector('.opblock-tag__name'));\n" +
                "             candidates.push(section.querySelector('button'));\n" +
                "             // initial clicks immediately\n" +
                "             for(const h of candidates.filter(Boolean)){\n" +
                "               try{ h.click(); } catch(e){}\n" +
                "             }\n" +
                " \n" +
                "            // Force-open child opblocks (in case UI re-renders and toggles them)\n" +
                "            function forceOpenChildren(){\n" +
                "              try{\n" +
                "                const opblocks = section.querySelectorAll('.opblock');\n" +
                "                opblocks.forEach(ob => {\n" +
                "                  try{\n" +
                "                    ob.classList.add('is-open');\n" +
                "                    // if there is a toggle button, set aria-expanded\n" +
                "                    const btn = ob.querySelector('button');\n" +
                "                    if(btn) btn.setAttribute('aria-expanded','true');\n" +
                "                    // ensure body is visible\n" +
                "                    const body = ob.querySelector('.opblock-body');\n" +
                "                    if(body) { body.style.display = 'block'; body.style.height = 'auto'; }\n" +
                "                  }catch(e){}\n" +
                "                });\n" +
                "              }catch(e){}\n" +
                "            }\n" +
                "\n" +
                "            forceOpenChildren();\n" +
                "\n" +
                "            // schedule a delayed click and repeated scrolls to counter UI re-render that snaps back\n" +
                "            setTimeout(()=>{\n" +
                "              try{ for(const h of candidates.filter(Boolean)){ try{ h.click(); }catch(e){} } }catch(e){}\n" +
                "              try{ section && section.scrollIntoView({behavior:'auto', block:'start'}); }catch(e){}\n" +
                "              forceOpenChildren();\n" +
                "            }, 150);\n" +
                "\n" +
                "            // retry loop to keep section open: click headers and force-open children until a child reports open\n" +
                "            const maxAttempts = 60; // extended\n" +
                "            let attempt = 0;\n" +
                "            const reopenInterval = setInterval(()=>{\n" +
                "              attempt++;\n" +
                "              try{\n" +
                "                const openFound = section.querySelector('.opblock.is-open') || section.querySelector('[aria-expanded=\"true\"]');\n" +
                "                if(openFound){ clearInterval(reopenInterval); return; }\n" +
                "                for(const h of candidates.filter(Boolean)){\n" +
                "                  try{ h.click(); }catch(e){}\n" +
                "                }\n" +
                "                try{ section && section.scrollIntoView({behavior:'auto', block:'start'}); }catch(e){}\n" +
                "                forceOpenChildren();\n" +
                "              }catch(e){}\n" +
                "              if(attempt>maxAttempts) clearInterval(reopenInterval);\n" +
                "            }, 200);\n" +
                "\n" +
                "            // persistent observer: re-apply forceOpenChildren when the section subtree changes (for a limited time)\n" +
                "            (function(){\n" +
                "              let applied = 0;\n" +
                "              const moRoot = section;\n" +
                "              try{\n" +
                "                const mo = new MutationObserver((mutations)=>{\n" +
                "                  try{ forceOpenChildren(); }catch(e){}\n" +
                "                  applied++;\n" +
                "                  if(applied>120) try{ mo.disconnect(); }catch(e){}\n" +
                "                });\n" +
                "                mo.observe(moRoot, { childList:true, subtree:true, attributes:true });\n" +
                "                // stop observer after duration\n" +
                "                setTimeout(()=>{ try{ mo.disconnect(); }catch(e){} }, 18000);\n" +
                "              }catch(e){}\n" +
                "            })();\n" +
                "\n" +
                "            // Make browser stop auto-restoring scroll and enforce target scroll frequently for a short period\n" +
                "            try{ if(window.history && 'scrollRestoration' in window.history) window.history.scrollRestoration = 'manual'; }catch(e){}\n" +
                "            // strong enforcement via rAF for ~6s\n" +
                "            (function(){\n" +
                "              const start = Date.now();\n" +
                "              const duration = 6000;\n" +
                "              let lastClickTime = 0;\n" +
                "              function enforce(){\n" +
                "                try{\n" +
                "                  const now = Date.now();\n" +
                "                  // stop if section appears open\n" +
                "                  const hasOpen = section.querySelector('.opblock.is-open') || section.querySelector('[aria-expanded=\"true\"]');\n" +
                "                  if(hasOpen) return;\n" +
                "                  // recompute target and scroll\n" +
                "                  try{\n" +
                "                    const r = section.getBoundingClientRect();\n" +
                "                    const targetY = window.scrollY + r.top - 80;\n" +
                "                    if(typeof targetY === 'number' && !Number.isNaN(targetY)){\n" +
                "                      window.scrollTo(0, Math.round(targetY));\n" +
                "                      document.documentElement.scrollTop = Math.round(targetY);\n" +
                "                      document.body.scrollTop = Math.round(targetY);\n" +
                "                    } else {\n" +
                "                      section && section.scrollIntoView({behavior:'auto', block:'start'});\n" +
                "                    }\n" +
                "                  }catch(e){ try{ section && section.scrollIntoView({behavior:'auto', block:'start'}); }catch(e){} }\n" +
                "                  // periodic clicks\n" +
                "                  if(now - lastClickTime > 250){\n" +
                "                    for(const h of candidates.filter(Boolean)){\n" +
                "                      try{ h.click(); }catch(e){}\n" +
                "                    }\n" +
                "                    forceOpenChildren();\n" +
                "                    lastClickTime = now;\n" +
                "                  }\n" +
                "                }catch(e){}\n" +
                "                if(Date.now() - start < duration) requestAnimationFrame(enforce);\n" +
                "              }\n" +
                "              requestAnimationFrame(enforce);\n" +
                "            })();\n" +
                "          }catch(e){ console.warn('[swagger-custom] scrollAndOpen click error', e); }\n" +
                "         }\n" +
                "\n" +
                "        // Retry while UI renders\n" +
                "        if(tryFindAndScroll()) return;\n" +
                "        // expose helper for debugging from browser console\n" +
                "        try{ window._scrollToSwaggerTag = scrollToTag; } catch(e){}\n" +
                "        let attempts = 0;\n" +
                "        const id = setInterval(()=>{\n" +
                "          attempts++;\n" +
                "          if(tryFindAndScroll() || attempts>120) clearInterval(id);\n" +
                "        }, 200);\n" +
                "      }\n" +
                "\n" +
                "      // Enhanced controllers table: search + sortable headers + nicer styling\n" +
                "      if(ul){\n" +
                "        ul.innerHTML = '';\n" +
                "        const container = document.createElement('div'); container.className = 'controller-container';\n" +
                "        // search box\n" +
                "        const searchWrap = document.createElement('div'); searchWrap.className = 'controller-search';\n" +
                "        const searchInput = document.createElement('input'); searchInput.type = 'search'; searchInput.placeholder = 'Filter controllers...'; searchInput.setAttribute('aria-label','Filter controllers');\n" +
                "        searchWrap.appendChild(searchInput);\n" +
                "        container.appendChild(searchWrap);\n" +
                "\n" +
                "        // actions row (small note)\n" +
                "        const actions = document.createElement('div'); actions.className = 'controller-actions small-muted'; actions.textContent = tags.length + ' controllers'; container.appendChild(actions);\n" +
                "\n" +
                "        // table\n" +
                "        const table = document.createElement('table'); table.className = 'controller-table';\n" +
                "        const thead = document.createElement('thead');\n" +
                "        thead.innerHTML = '<tr>' +\n" +
                "          '<th class=\"col-index\">#</th>' +\n" +
                "          '<th class=\"col-name\">Controller</th>' +\n" +
                "          '<th class=\"col-ops\">Operations</th>' +\n" +
                "          '</tr>';\n" +
                "        table.appendChild(thead);\n" +
                "        const tbody = document.createElement('tbody'); table.appendChild(tbody);\n" +
                "\n" +
                "        // state for sorting\n" +
                "        let currentSort = { key: 'name', dir: 1 }; // dir: 1 asc, -1 desc\n" +
                "\n" +
                "        function buildRows(filter){\n" +
                "          tbody.innerHTML = '';\n" +
                "          let rows = tags.map((t, idx) => ({ name: t, ops: counts[t] || 0, idx }));\n" +
                "          if(filter){ const f = filter.toLowerCase(); rows = rows.filter(r=> r.name.toLowerCase().indexOf(f)!==-1); }\n" +
                "          // sort\n" +
                "          if(currentSort.key === 'name') rows.sort((a,b)=> currentSort.dir * a.name.localeCompare(b.name, undefined, {sensitivity:'base'}));\n" +
                "          else rows.sort((a,b)=> currentSort.dir * (a.ops - b.ops));\n" +
                "          if(rows.length===0){ const tr = document.createElement('tr'); const td = document.createElement('td'); td.colSpan=3; td.className='controller-empty'; td.textContent='No controllers/tags found'; tr.appendChild(td); tbody.appendChild(tr); return; }\n" +
                "          rows.forEach((r,i)=>{\n" +
                "            const tr = document.createElement('tr');\n" +
                "            const td0 = document.createElement('td'); td0.className='col-index'; td0.textContent = String(i+1);\n" +
                "            const td1 = document.createElement('td'); td1.className='col-name'; td1.textContent = r.name;\n" +
                "            const td2 = document.createElement('td'); td2.className='col-ops'; td2.textContent = String(r.ops);\n" +
                "            tr.appendChild(td0); tr.appendChild(td1); tr.appendChild(td2);\n" +
                "            tbody.appendChild(tr);\n" +
                "          });\n" +
                "        }\n" +
                "\n" +
                "        // header click to sort\n" +
                "        thead.querySelector('.col-name').style.cursor='pointer'; thead.querySelector('.col-ops').style.cursor='pointer';\n" +
                "        thead.querySelector('.col-name').addEventListener('click', ()=>{ currentSort.key='name'; currentSort.dir = -currentSort.dir; buildRows(searchInput.value); });\n" +
                "        thead.querySelector('.col-ops').addEventListener('click', ()=>{ currentSort.key='ops'; currentSort.dir = -currentSort.dir; buildRows(searchInput.value); });\n" +
                "\n" +
                "        // search\n" +
                "        searchInput.addEventListener('input', ()=>{ buildRows(searchInput.value); });\n" +
                "\n" +
                "        // initial render\n" +
                "        buildRows('');\n" +
                "\n" +
                "        ul.appendChild(container);\n" +
                "        container.appendChild(table);\n" +
                "      }\n" +
                "    }catch(e){ console.warn('Could not fetch api-docs for controllers list', e); }\n" +
                "  }\n" +
                "  // render controllers ASAP (before UI boot)\n" +
                "  fetchAndRenderControllers();\n" +
                "  async function ensureAndInit(){\n" +
                "    try{\n" +
                "      // Ensure bundle exists, try local first then CDN\n" +
                "      if (typeof SwaggerUIBundle === 'undefined') {\n" +
                "        try{ await loadScript('/webjars/swagger-ui/swagger-ui-bundle.js'); } catch(e){ await loadScript('https://unpkg.com/swagger-ui-dist@4/swagger-ui-bundle.js'); }\n" +
                "      }\n" +
                "      // Ensure preset exists, try local then CDN\n" +
                "      if (typeof SwaggerUIStandalonePreset === 'undefined') {\n" +
                "        try{ await loadScript('/webjars/swagger-ui/swagger-ui-standalone-preset.js'); } catch(e){ await loadScript('https://unpkg.com/swagger-ui-dist@4/swagger-ui-standalone-preset.js'); }\n" +
                "      }\n" +
                "      if (typeof SwaggerUIBundle === 'undefined' || typeof SwaggerUIStandalonePreset === 'undefined') {\n" +
                "        showError('Swagger UI scripts could not be loaded. Check network or classpath.');\n" +
                "        return;\n" +
                "      }\n" +
                "      // init\n" +
                "      try{\n" +
                "        const ui = SwaggerUIBundle({\n" +
                "          url: '/v3/api-docs',\n" +
                "          dom_id: '#swagger-ui',\n" +
                "          presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],\n" +
                "          layout: 'StandaloneLayout',\n" +
                "          // sort tag groups (controllers) alphabetically\n" +
                "          tagsSorter: 'alpha',\n" +
                "          // sort operations within a tag alphabetically\n" +
                "          operationsSorter: 'alpha',\n" +
                "          displayRequestDuration: true,\n" +
                "          defaultModelsExpandDepth: -1\n" +
                "        });\n" +
                "        window.ui = ui;\n" +
                "        try {\n" +
                "          // Build a tag->element map once the UI renders, and keep it updated via MutationObserver.\n" +
                "          function buildTagMap(){\n" +
                "            try{\n" +
                "              window._swaggerTagMap = window._swaggerTagMap || {};\n" +
                "              window._swaggerTagMap = {};\n" +
                "              // broaden selectors and include possible container titles/ids\n" +
                "              var selectors = ['.opblock-tag','.opblock-tag__name','.opblock-summary','.opblock .opblock-summary','.tag','.resource','.section-title','.opblock-title','[data-tag]','[id^=\"operations-tag-\"]','h2','h3','h4','summary'];\n" +
                "              var seen = new Set();\n" +
                "              selectors.forEach(function(sel){\n" +
                "                Array.from(document.querySelectorAll(sel)).forEach(function(el){\n" +
                "                  try{\n" +
                "                    var raw = (el.getAttribute('data-tag') || el.getAttribute('aria-label') || el.textContent || '').replace(/\\s+/g,' ').trim();\n" +
                "                    if(!raw) return;\n" +
                "                    // strip trailing counts like 'Name (12)'\n" +
                "                    raw = raw.replace(/\\s*\\(\\d+\\)\\s*$/, '').trim();\n" +
                "                    var key = raw.toLowerCase();\n" +
                "                    if(seen.has(key)) return;\n" +
                "                    seen.add(key);\n" +
                "                    window._swaggerTagMap[key] = el;\n" +
                "                    // also map id-based slug form\n" +
                "                    var slug = raw.toLowerCase().replace(/[^a-z0-9]+/g,'-').replace(/(^-|-$)/g,'');\n" +
                "                    if(slug) window._swaggerTagMap[slug] = el;\n" +
                "                    // also map by any id on element\n" +
                "                    if(el.id) window._swaggerTagMap[el.id.toLowerCase()] = el;\n" +
                "                  }catch(e){ /* ignore per-element */ }\n" +
                "                });\n" +
                "              });\n" +
                "              console.info('[swagger-custom] built tag map with keys=', Object.keys(window._swaggerTagMap).slice(0,200));\n" +
                "            }catch(e){ console.warn('[swagger-custom] buildTagMap error', e); }\n" +
                "          }\n" +
                "          setTimeout(buildTagMap, 500);\n" +
                "          try{\n" +
                "            var root = document.getElementById('swagger-ui') || document.body;\n" +
                "            var mo = new MutationObserver(function(){ buildTagMap(); });\n" +
                "            mo.observe(root, { childList: true, subtree: true });\n" +
                "          }catch(e){ /* ignore observer errors */ }\n" +
                "        } catch(e){ console.warn('[swagger-custom] tag map init failed', e); }\n" +
                "      } catch(e){ console.error('initUI error', e); showError('Swagger UI initialization error: '+e); }\n" +
                "    } catch(err){ console.error('ensureAndInit error', err); showError('Error loading Swagger UI scripts: '+ err); }\n" +
                "  }\n" +
                "  // Start the loader\n" +
                "  ensureAndInit();\n" +
                "})();\n" +
                "</script>\n" +
                "<script src=\"/swagger-custom.js\"></script>\n" +
                "</body>\n" +
                "</html>\n";

        return ResponseEntity.ok(html);
    }
}

