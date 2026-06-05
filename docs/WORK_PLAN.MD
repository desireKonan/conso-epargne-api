## Workflow Orchestration
### 1. Default Planning Mode
Enter planning mode for ANY non-trivial task (3 or more steps, or architectural decisions)
If something deviates from the plan, STOP and replan immediately; do not continue pushing forward
Use planning mode for verification steps, not just for construction
Write detailed specifications upfront to reduce ambiguity

#### 1.1. Somes Rules for planning mode.
1) Always analyse the common-service module because all core of application is here.
2) Scan v2 package in all common-service to use new entities, repositories, services or another thing (v2 module contains all new database design and refactoring).
3) Follow some strict rules:
--- Always use an DTO for return type in the service interface: if DTO don't exist create an DTO strictly based on Entity associated.
--- Always create an mapper for Entity and DTO associated to bind it.
--- Stricty use this format for interface like base model not absolute: 
```java
public interface EntityNameService {
    Page<DTO> getAll(FilterCriteria filterCriteria, Pageable pageable);

    DTO getById(IDType id);

    DTO create(CreateDTO createDTO);

    DTO update(IDType id, UpdateDTO updateDTO);

    void delete(IDType id);
}
```
--- Always use SOLID principles to design the code.


4) Follow this architecture module model:
```
module-name/
|--- client (For all external services calls)
|--- common
|----- exceptions
|----- utils
|----- config
|----- dtos
|----- entities
|----- repositories
|----- services
|--- domain
|---- domain_name
|----- dtos
|----- repositories
|----- services
|----- entities
|----- listeners (If needed)
```

5) Just always create a file for all updates, optimisation in tasks/updates.md with an explanation of what was done and why.
```
- [ ] Update 1: Explanation
- [ ] Update 2: Explanation
- [ ] Refactoring 1: Explanation with code example and schema if necessary
```

### 2. Subagent Strategy
Use subagents liberally to keep the main context window clean
Delegate search, exploration, and parallel analysis to subagents
For complex problems, distribute more work via subagents
One tool (task) per subagent for focused execution
### 3. Self-improvement loop
After EVERY user correction: update tasks/lessons.md with the reason
Write rules for yourself to avoid making the same mistake
Iterate relentlessly on these lessons until the error rate drops
Review the lessons at the start of each session for the relevant project
### 4. Verify Before Marking as Complete
Never mark a task as complete without proving that it works
Highlight the differences in behavior between the original state and your changes when relevant
Ask yourself: < Would a senior engineer approve this? >>>
Run tests, check logs, demonstrate correctness
### 5. Requirement for elegance (balanced)
For non-trivial changes, pause and ask yourself << Is there a more elegant way? >>>
If a fix seems shaky: “Knowing what I know now, would I implement the elegant solution?”
Avoid simple, obvious fixes; don’t over-engineer
Question your own work before presenting it
### 6. Independent Bug Fixing
When a bug is reported to you, fix it immediately. Don’t ask to review the code manually
Point to logs, errors, and failed tests, then resolve them
No context switch is required on the user’s part
Fix failing CI tests without being told how
## Task Management
1. Plan first: Write the plan in tasks/todo.md with actionable items
2. **Review the plan: Validate it before starting implementation
3. Track progress: Check off completed items as you go
4. Explain changes: Provide a high-level summary at each stage
5. Document results: Add a review section to tasks/todo.md
6. Capture lessons: Update tasks/lessons.md after fixes
## Core Principles
Simplicity first: Make every change as simple as possible. Minimal impact on the code.
**No shortcuts*: Find the root causes. No temporary fixes. Senior developer standards.
**Minimal impact: Changes should only affect what is necessary. Avoid introducing bugs.