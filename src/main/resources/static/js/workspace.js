// workspace.js - Client-side JavaScript for SQL editor

document.addEventListener('DOMContentLoaded', function() {
    // Initialize CodeMirror SQL editor
    const sqlEditor = CodeMirror.fromTextArea(document.getElementById('sql-editor'), {
        mode: 'text/x-sql',
        theme: 'darcula',
        lineNumbers: true,
        autoCloseBrackets: true,
        matchBrackets: true,
        indentWithTabs: true,
        indentUnit: 4,
        tabSize: 4,
        lineWrapping: true,
        extraKeys: {
            "Ctrl-Space": "autocomplete",
            "Ctrl-Enter": function(cm) {
                executeQuery();
            }
        },
        hintOptions: {
            tables: {
                // These would be populated dynamically from your schema
                "students": ["student_id", "username", "email", "password", "intro_content"],
                "problems": ["problem_id", "title", "description", "difficulty", "category", "instructor_id"],
                "submissions": ["submission_id", "student_id", "problem_id", "submission_code", "status", "score"]
            }
        }
    });

    // Set editor height
    sqlEditor.setSize(null, 200);

    // Function to execute the SQL query
    function executeQuery() {
        const query = sqlEditor.getValue();

        // Show loading indicator
        document.getElementById('result-loading').style.display = 'block';
        document.getElementById('result-container').style.display = 'none';

        // Send query to server
        fetch('/api/workspace/execute', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                query: query,
                problemId: document.getElementById('problem-id').value
            })
        })
            .then(response => response.json())
            .then(data => {
                // Hide loading indicator
                document.getElementById('result-loading').style.display = 'none';
                document.getElementById('result-container').style.display = 'block';

                if (data.error) {
                    displayError(data.error);
                } else {
                    displayResults(data.results);

                    // If this is a submission, check if it's correct
                    if (data.isCorrect !== undefined) {
                        displayFeedback(data.isCorrect, data.feedback);
                    }
                }
            })
            .catch(error => {
                // Hide loading indicator
                document.getElementById('result-loading').style.display = 'none';
                displayError('An error occurred while executing the query: ' + error.message);
            });
    }

    // Function to display query errors
    function displayError(errorMessage) {
        const resultContainer = document.getElementById('result-container');
        resultContainer.innerHTML = `
            <div class="alert alert-danger">
                <strong>Error:</strong> ${errorMessage}
            </div>
        `;
        resultContainer.style.display = 'block';
    }

    // Function to display query results
    function displayResults(results) {
        const resultContainer = document.getElementById('result-container');

        if (!results || results.length === 0) {
            resultContainer.innerHTML = '<div class="alert alert-info">Query executed successfully. No results returned.</div>';
            return;
        }

        // Create table for results
        let html = '<div class="table-responsive"><table class="table table-striped table-bordered">';

        // Add headers
        html += '<thead><tr>';
        for (const key in results[0]) {
            html += `<th>${key}</th>`;
        }
        html += '</tr></thead>';

        // Add data rows
        html += '<tbody>';
        for (const row of results) {
            html += '<tr>';
            for (const key in results[0]) {
                html += `<td>${row[key] !== null ? row[key] : 'NULL'}</td>`;
            }
            html += '</tr>';
        }
        html += '</tbody></table></div>';

        resultContainer.innerHTML = html;
    }

    // Function to display feedback for submissions
    function displayFeedback(isCorrect, feedback) {
        const feedbackContainer = document.getElementById('feedback-container');

        if (isCorrect) {
            feedbackContainer.innerHTML = `
                <div class="alert alert-success">
                    <strong>Correct!</strong> ${feedback}
                </div>
            `;
        } else {
            feedbackContainer.innerHTML = `
                <div class="alert alert-warning">
                    <strong>Not quite right.</strong> ${feedback}
                </div>
            `;
        }

        feedbackContainer.style.display = 'block';
    }

    // Attach execute button event
    document.getElementById('execute-btn').addEventListener('click', executeQuery);

    // Attach submit button event
    document.getElementById('submit-btn').addEventListener('click', function() {
        // First execute the query
        executeQuery();

        // Then submit it as a solution
        const query = sqlEditor.getValue();

        fetch('/api/submissions/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                query: query,
                problemId: document.getElementById('problem-id').value
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    displayError(data.error);
                } else {
                    // Show feedback and notification
                    displayFeedback(data.isCorrect, data.feedback);

                    // If correct, show congratulations
                    if (data.isCorrect) {
                        // Maybe show confetti or a modal
                    }
                }
            })
            .catch(error => {
                displayError('An error occurred while submitting: ' + error.message);
            });
    });

    // Add custom SQL validation
    sqlEditor.on('change', function() {
        const value = sqlEditor.getValue().toLowerCase();

        // Basic validation examples - you would expand these
        if (value.includes('drop table') || value.includes('drop database')) {
            // Highlight as an error
            sqlEditor.markText(
                {line: 0, ch: 0},
                {line: sqlEditor.lineCount(), ch: 0},
                {className: 'sql-danger'}
            );
        }
    });
});