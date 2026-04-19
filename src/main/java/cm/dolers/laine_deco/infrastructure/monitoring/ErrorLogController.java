package cm.dolers.laine_deco.infrastructure.monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/errors")
public class ErrorLogController {

    private final ErrorLogStore store;

    public ErrorLogController(ErrorLogStore store) {
        this.store = store;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("errors", store.getAll());
        model.addAttribute("totalErrors", store.countByLevel("ERROR"));
        model.addAttribute("totalWarns",  store.countByLevel("WARN"));
        return "admin/errors";
    }

    @PostMapping("/clear")
    public String clear() {
        store.clear();
        return "redirect:/admin/errors";
    }
}