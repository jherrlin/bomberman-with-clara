(setq gnutls-algorithm-priority "NORMAL:-VERS-TLS1.3")
(require 'package)
(setq package-enable-at-startup nil)
(setq package-check-signature nil)
(add-to-list 'package-archives '("melpa" . "http://melpa.org/packages/"))
(add-to-list 'package-archives '("gnu" . "http://elpa.gnu.org/packages/"))
(package-initialize)

(unless (package-installed-p 'use-package)
  (package-refresh-contents)
  (package-install 'use-package)
  (package-install 'gnu-elpa-keyring-update))

(eval-when-compile
  (require 'use-package))

(setq use-package-always-ensure t)
(prefer-coding-system 'utf-8)
(setq locale-coding-system 'utf-8)
(set-default-coding-systems 'utf-8)
(setq-default fill-column 80)
(global-unset-key (kbd "C-z"))
(setq confirm-kill-emacs 'y-or-n-p)
(setq-default truncate-lines nil)
(setq-default global-visual-line-mode t)
(set-default 'truncate-lines t)
(show-paren-mode t)
(setq show-paren-delay 0.0)
(winner-mode 1)
(setq ring-bell-function 'ignore)
(setq-default indent-tabs-mode nil)
(setq-default tab-width 3)
(when (fboundp 'menu-bar-mode) (menu-bar-mode -1))
(when (fboundp 'tool-bar-mode) (tool-bar-mode -1))
(when (fboundp 'scroll-bar-mode) (scroll-bar-mode -1))
(when (fboundp 'horizontal-scroll-bar-mode) (horizontal-scroll-bar-mode -1))
(add-hook 'before-save-hook 'delete-trailing-whitespace)
(setq grep-command "grep -rnw . --exclude-dir=node_modules -e ")
(setq inhibit-startup-screen t
      inhibit-startup-message t
      inhibit-startup-echo-area-message t
      initial-scratch-message nil)
(setq create-lockfiles nil)
(setq auto-save-default nil)
(setq make-backup-files nil)
(setq backup-directory-alist
      `((".*" . ,temporary-file-directory)))
(setq auto-save-file-name-transforms
      `((".*" ,temporary-file-directory t)))

(use-package zenburn-theme
  :pin melpa
  :config
  (load-theme 'zenburn t))

(use-package cider
  :pin melpa
  :config
  (setq cider-offer-to-open-cljs-app-in-browser nil)
  (setq org-babel-clojure-backend 'cider))

(use-package clj-refactor
  :pin melpa)
