package cm.dolers.laine_deco.infrastructure.monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/errors")
public class ErrorLogController {

    private final LogStore store;

    public ErrorLogController(LogStore store) {
        this.store = store;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("logs", store.getAll());
        model.addAttribute("totalErrors",   store.countByLevel("ERROR"));
        model.addAttribute("totalWarns",    store.countByLevel("WARN"));
        model.addAttribute("totalInfos",    store.countByCategory("INFO"));
        model.addAttribute("totalSql",      store.countByCategory("SQL"));
        model.addAttribute("totalSecurity", store.countByCategory("SECURITY"));
        model.addAttribute("totalHttp",     store.countByCategory("HTTP"));
        model.addAttribute("totalDebug",    store.countByCategory("DEBUG"));
        return "admin/errors";
    }

    @PostMapping("/clear")
    public String clear() {
        store.clear();
        return "redirect:/admin/errors";
    }
}