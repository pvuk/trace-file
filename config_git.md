#Issue Fix
Since logcli.exe (125MB) is already committed and blocking your push, just moving the file forward won't fix it — the large file is still sitting in your git history. Here's how to properly resolve this:

1. Move the file out of the repo folder
Move logcli.exe from src/main/resources to somewhere outside the git repo entirely — e.g. D:\Backup\Softwares\logcli-windows-amd64 (where you already have a copy) or a tools folder outside the project directory.

2. Remove it from git tracking
In a terminal at your repo root, run: git rm --cached src/main/resources/logcli.exe. This untracks the file without deleting your local copy.

3. Add it to .gitignore
Add a line 'src/main/resources/logcli.exe' (or '*.exe' if you never want to commit binaries) to your .gitignore so it doesn't get re-added accidentally.

4. Strip it from your commit history
The file is already baked into your 12 unpushed commits, so git rm alone won't let you push — GitHub still rejects based on history. Use git filter-repo (recommended) or BFG Repo-Cleaner to purge logcli.exe from all commits: git filter-repo --path src/main/resources/logcli.exe --invert-paths

5. Update any code references
Search your codebase (e.g. in application.yaml, config files, or Java code) for any hardcoded path pointing to src/main/resources/logcli.exe and update it to point to the new external location, or better, make it a configurable/environment path.

6. Force-push the cleaned history
Since history was rewritten, push with: git push origin main --force. Coordinate with any collaborators first, since this rewrites shared history.

7. Commit the .gitignore and any code changes
Stage and commit your .gitignore update and any path changes as a normal commit, then push normally.

The key issue: logcli.exe is already baked into all 12 of your unpushed commits, so simply deleting/moving it now and committing that change won't help — GitHub still sees the 125MB blob in history when you push.

Quickest path for your situation (since these 12 commits haven't been pushed yet):

If you don't mind losing this local commit history and starting clean from your last pushed state, it's simpler to:

	Note down what files you've changed
	git reset --soft <last-pushed-commit-hash> to uncommit everything (keeping your changes as uncommitted)
	Move logcli.exe out, add it to .gitignore
	Re-commit everything cleanly without the exe ever entering history
	Push normally — no force-push needed

This avoids the git filter-repo/force-push complexity entirely since the bad commits haven't left your machine yet. Want me to walk through the exact commands for this route?