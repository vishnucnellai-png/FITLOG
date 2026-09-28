
const $ = (selector) => document.querySelector(selector);

const today = new Date().toISOString().slice(0, 10);

let users = [];


// =====================================================
// API REQUEST
// =====================================================

async function apiRequest(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json"
        },
        ...options
    });

    if (!response.ok) {
        const error = await response
            .json()
            .catch(() => ({
                message: "Request failed."
            }));

        throw new Error(
            error.message || "Request failed."
        );
    }

    return response.status === 204
        ? null
        : response.json();
}


// =====================================================
// NOTIFICATION
// =====================================================

function notify(message, type = "success") {
    const element = $("#message");

    element.textContent = message;
    element.className = `message show ${type}`;

    setTimeout(() => {
        element.className = "message";
    }, 3500);
}


// =====================================================
// USER OPTIONS
// =====================================================

function options(selected = "") {
    const userOptions = users
        .map(
            (user) =>
                `<option value="${user.id}" ${
  String(user.id) === String(selected)
      ? "selected"
      : ""
}>
${user.name}
</option>`
        )
        .join("");

    return `
<option value="">Select user</option>
${userOptions}
`;
}


function fillUsers() {
    document
        .querySelectorAll(
            ".user-select, #dashboard-user, #summary-user"
        )
        .forEach((select) => {
            const value = select.value;

            select.innerHTML = options(value);
        });
}


// =====================================================
// LOAD USERS
// =====================================================

async function loadUsers() {
    users = await apiRequest("/api/users");

    fillUsers();

    $("#users-table").innerHTML =
        users
            .map(
                (user) => `
<tr>
<td>${user.name}</td>

<td>${user.email}</td>

<td>
  <button
      class="table-action"
      data-edit-users="${user.id}"
  >
    Edit
  </button>

  <button
      class="table-action delete"
      data-delete-users="${user.id}"
  >
    Delete
  </button>
</td>
</tr>
`
            )
            .join("") ||
        `
<tr>
<td colspan="3">
    No users yet.
</td>
</tr>
`;
}


// =====================================================
// LOAD WORKOUTS
// =====================================================

async function loadWorkouts() {
    const workouts = await apiRequest(
        "/api/workouts"
    );

    $("#workouts-table").innerHTML =
        workouts
            .map(
                (workout) => `
<tr>

<td>
${workout.type}
</td>

<td>
  ${workout.duration}
</td>

<td>
  ${workout.caloriesBurned}
</td>

<td>
  ${workout.date}
</td>

<td>
  <button
      class="table-action"
      data-edit-workouts="${workout.id}"
  >
    Edit
  </button>

  <button
      class="table-action delete"
      data-delete-workouts="${workout.id}"
  >
    Delete
  </button>
</td>

</tr>
`
            )
            .join("") ||
        `
<tr>
<td colspan="5">
    No workouts yet.
</td>
</tr>
`;
}


// =====================================================
// LOAD OTHER COLLECTIONS
// =====================================================

async function loadCollection(name, fields) {
    const rows = await apiRequest(
        `/api/${name}`
    );

    $(`#${name}-table`).innerHTML =
        rows
            .map(
                (item) => `
<tr>

${fields
    .map(
        (field) =>
            `<td>${item[field]}</td>`
    )
    .join("")}

<td>
  <button
      class="table-action"
      data-edit-${name}="${item.id}"
  >
    Edit
  </button>

  <button
      class="table-action delete"
      data-delete-${name}="${item.id}"
  >
    Delete
  </button>
</td>

</tr>
`
            )
            .join("") ||
        `
<tr>
<td colspan="6">
    No ${name} yet.
</td>
</tr>
`;

    return rows;
}


// =====================================================
// LOAD ALL DATA
// =====================================================

async function loadAll() {
    await loadUsers();

    await Promise.all([
        loadWorkouts(),

        loadCollection("meals", [
            "foodItem",
            "quantity",
            "calories",
            "date"
        ]),

        loadCollection("goals", [
            "weeklyWorkoutTarget"
        ])
    ]);
}


// =====================================================
// RESET FORM
// =====================================================

function resetForm(form) {
    form.reset();

    form.elements.id.value = "";

    form.querySelector(
        "button[type=submit]"
    ).textContent =
        `Add ${form.id
    .replace("-form", "")
    .replace(
        /^./,
        (char) => char.toUpperCase()
    )}`;
}


// =====================================================
// FORM DATA
// =====================================================

function formData(form) {
    const data = Object.fromEntries(
        new FormData(form)
    );

    delete data.id;

    Object.keys(data).forEach((key) => {
        if (
            [
                "duration",
                "caloriesBurned",
                "quantity",
                "calories",
                "weeklyWorkoutTarget",
                "userId"
            ].includes(key)
        ) {
            data[key] = Number(data[key]);
        }
    });

    return data;
}


// =====================================================
// SUBMIT FORM
// =====================================================

async function submitForm(event, name) {
    event.preventDefault();

    const form = event.currentTarget;

    const id = form.elements.id.value;

    try {
        await apiRequest(
            `/api/${name}${id ? `/${id}` : ""}`,
            {
                method: id
                    ? "PUT"
                    : "POST",

                body: JSON.stringify(
                    formData(form)
                )
            }
        );

        notify(
            `${name.slice(0, -1)} saved.`
        );

        resetForm(form);
        await loadWorkouts();


        // =============================================
        // REFRESH THE CORRECT LIST
        // =============================================

        if (name === "workouts") {
            await loadWorkouts();
        } else if (name === "meals") {
            await loadCollection("meals", [
                "foodItem",
                "quantity",
                "calories",
                "date"
            ]);
        } else if (name === "goals") {
            await loadCollection("goals", [
                "weeklyWorkoutTarget"
            ]);
        } else if (name === "users") {
            await loadUsers();
        }

    } catch (error) {
        notify(
            error.message,
            "error"
        );
    }
}


// =====================================================
// DELETE RECORD
// =====================================================

async function remove(name, id) {
    if (!confirm("Delete this record?")) {
        return;
    }

    try {
        await apiRequest(
            `/api/${name}/${id}`,
{
  method: "DELETE"
}
);

notify("Record deleted.");

// Refresh only the required list
if (name === "workouts") {
  await loadWorkouts();
} else {
  await loadAll();
}

} catch (error) {
  notify(
      error.message,
      "error"
  );
}
}


// =====================================================
// EDIT RECORD
// =====================================================

async function edit(name, id) {
  try {
    const item = await apiRequest(
        `/api/${name}/${id}`
    );

    const form = $(
        `#${name.slice(0, -1)}-form`
    );

    Object.entries(item).forEach(
        ([key, value]) => {
          if (form.elements[key]) {
            form.elements[key].value =
                value ?? "";
          }
        }
    );

    form.elements.id.value = id;

    form.querySelector(
        "button[type=submit]"
    ).textContent =
        `Update ${name.slice(0, -1)}`;

    document
        .querySelector(
            `[data-section="${name}"]`
        )
        .click();

  } catch (error) {
    notify(
        error.message,
        "error"
    );
  }
}


// =====================================================
// SUMMARY DISPLAY
// =====================================================

function renderSummary(data) {
  $("#summary-result").className =
      "summary-result";

  $("#summary-result").innerHTML = `

        <p class="eyebrow">
            ${data.startDate}
            to
            ${data.endDate}
        </p>

        <div class="summary-data">

            <div>
                <span>
                    Calories Consumed
                </span>

                <strong>
                    ${data.caloriesConsumed}
                </strong>
            </div>


            <div>
                <span>
                    Calories Burned
                </span>

                <strong>
                    ${data.caloriesBurned}
                </strong>
            </div>


            <div>
                <span>
                    Net Calories
                </span>

                <strong>
                    ${data.netCalories}
                </strong>
            </div>


            <div>
                <span>
                    Workout Count
                </span>

                <strong>
                    ${data.workoutsCompleted}
                </strong>
            </div>


            <div>
                <span>
                    Weekly Goal
                </span>

                <strong>
                    ${data.weeklyWorkoutGoal}
                </strong>
            </div>

        </div>
    `;
}


// =====================================================
// SUMMARY
// =====================================================

async function summary(
    kind,
    dashboard = false
) {
  const userId = $(
      dashboard
          ? "#dashboard-user"
          : "#summary-user"
  ).value;

  const date = $(
      dashboard
          ? "#dashboard-date"
          : "#summary-date"
  ).value;


  if (!userId) {
    notify(
        "Please select a user.",
        "error"
    );

    return;
  }


  try {
    const data = await apiRequest(
        `/api/summary/${kind}/${userId}?date=${date}`
    );


    // =============================================
    // DASHBOARD SUMMARY
    // =============================================

    if (dashboard) {

      $("#card-consumed").textContent =
          data.caloriesConsumed;

      $("#card-burned").textContent =
          data.caloriesBurned;

      $("#card-net").textContent =
          data.netCalories;

      $("#card-workouts").textContent =
          data.workoutsCompleted;

      $("#card-goal").textContent =
          data.weeklyWorkoutGoal;

      return;
    }


    // =============================================
    // NORMAL SUMMARY
    // =============================================

    renderSummary(data);

  } catch (error) {

    if (dashboard) {

      [
        "consumed",
        "burned",
        "net",
        "workouts",
        "goal"
      ].forEach((key) => {
        $(`#card-${key}`)
            .textContent = "-";
      });

      notify(
          error.message,
          "error"
      );

      return;
    }


    $("#summary-result").className =
        "summary-result empty";

    $("#summary-result").textContent =
        error.message;
  }
}


// =====================================================
// BUTTON EVENTS
// =====================================================

document.addEventListener(
    "click",
    (event) => {

      const button =
          event.target.closest("button");


      if (!button) {
        return;
      }


      // =============================================
      // SECTION NAVIGATION
      // =============================================

      if (button.dataset.section) {

        document
            .querySelectorAll(".nav-link")
            .forEach((link) => {

              link.classList.toggle(
                  "active",
                  link === button
              );

            });


        document
            .querySelectorAll(".page-section")
            .forEach((section) => {

              section.classList.toggle(
                  "active",
                  section.id ===
                  button.dataset.section
              );

            });
      }


      // =============================================
      // EDIT / DELETE
      // =============================================

      for (
          const name of [
        "users",
        "workouts",
        "meals",
        "goals"
      ]
          ) {

        const capitalized =
            name[0].toUpperCase() +
            name.slice(1);


        // -----------------------------------------
        // DELETE
        // -----------------------------------------

        const deleteId =
            button.dataset[
                `delete${capitalized}`
                ];


        if (deleteId) {
          remove(
              name,
              deleteId
          );
        }


        // -----------------------------------------
        // EDIT
        // -----------------------------------------

        const editId =
            button.dataset[
                `edit${capitalized}`
                ];


        if (editId) {
          edit(
              name,
              editId
          );
        }
      }


      // =============================================
      // CANCEL EDIT
      // =============================================

      if (
          button.classList.contains(
              "cancel-edit"
          )
      ) {
        resetForm(
            button.closest("form")
        );
      }
    }
);


// =====================================================
// FORM SUBMISSION
// =====================================================

[
  "user",
  "workout",
  "meal",
  "goal"
].forEach((name) => {

  const form =
      $(`#${name}-form`);

  if (!form) {
    return;
  }

  form.addEventListener(
      "submit",
      (event) => {

        submitForm(
            event,
            `${name}s`
        );

      }
  );
});


// =====================================================
// DATE INITIALIZATION
// =====================================================

$("#dashboard-date").value =
    today;

$("#summary-date").value =
    today;


// =====================================================
// DASHBOARD REFRESH
// =====================================================

$("#refresh-dashboard").addEventListener(
    "click",
    () => summary(
        "daily",
        true
    )
);


// =====================================================
// DAILY SUMMARY
// =====================================================

$("#daily-summary-button").addEventListener(
    "click",
    () => summary(
        "daily"
    )
);


// =====================================================
// WEEKLY SUMMARY
// =====================================================

$("#weekly-summary-button").addEventListener(
    "click",
    () => summary(
        "weekly"
    )
);


// =====================================================
// INITIAL DATA LOAD
// =====================================================

loadAll().catch(
    (error) => {
      notify(
          error.message,
          "error"
      );
    }
);
/*

### Important change

The old flow was:

```text
POST /api/workouts
       ↓
loadAll()
       ↓
loadUsers()
       ↓
loadWorkouts()
    ```

The new flow is:

```text
POST /api/workouts
       ↓
loadWorkouts()
       ↓
GET /api/workouts
       ↓
Update #workouts-table
    ```

So after clicking **Add Workout**, the workout table should update **without refreshing the browser**.

One thing to verify: your HTML must contain exactly:

```html
<tbody id="workouts-table"></tbody>
    ```

and your form must be:

```html
<form id="workout-form">
    ```

with field names matching:

```text
userId
type
duration
caloriesBurned
date
id
    ```

If those names match your backend, this JS is ready to use.

 */
