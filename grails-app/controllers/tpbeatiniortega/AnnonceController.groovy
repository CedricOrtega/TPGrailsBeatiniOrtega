package tpbeatiniortega

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class AnnonceController {

    AnnonceService annonceService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond annonceService.list(params), model:[annonceCount: annonceService.count()]
    }

    def show(Long id) {
        respond annonceService.get(id)
    }

    def create() {
        respond new Annonce(params)
    }

    def save(Annonce annonce) {
        println params
        if (annonce == null) {
            notFound()
            return
        }

        def file = request.getFile("featuredImageFile")
        System.out.println(file)

        file.transferTo(new File(grailsApplication.config.maconfig.assets_path+params.featuredImageFile.filename))

        String strFile = params.featuredImageFile.filename
        //annonce.addToIllustrations(new Illustration(filename: strFile.toString()))
        annonce.addToIllustrations(new Illustration(filename: strFile))
        try {
            annonceService.save(annonce)
        } catch (ValidationException e) {
            respond annonce.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'annonce.label', default: 'Annonce'), annonce.id])
                redirect annonce
            }
            '*' { respond annonce, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond annonceService.get(id)
    }

    def update(Annonce annonce) {
        println params
        if (annonce == null) {
            notFound()
            return
        }

        def file = request.getFile("featuredImageFile")
        System.out.println(file)

        file.transferTo(new File(grailsApplication.config.maconfig.assets_path+params.featuredImageFile.filename))

        String strFile = params.featuredImageFile.filename
        //annonce.addToIllustrations(new Illustration(filename: strFile.toString()))
        annonce.addToIllustrations(new Illustration(filename: strFile))

        try {
            annonceService.save(annonce)
        } catch (ValidationException e) {
            println e
            redirect view: '/index'
            return
            respond annonce.errors, view:'show'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'annonce.label', default: 'Annonce'), annonce.id])
                redirect annonce
            }
            '*'{ respond annonce, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        annonceService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'annonce.label', default: 'Annonce'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'annonce.label', default: 'Annonce'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def deleteIllustration()
    {

        def illustrationId = params.id
        def annonceId = params.annonceId

        def annonceInstance = Annonce.get(annonceId)
        def illustrationInstance = Illustration.get(illustrationId)
        println(illustrationInstance)
        println(annonceInstance)
        annonceInstance.removeFromIllustrations(illustrationInstance)
        annonceInstance.save(flush: true)
        // effacer le fichier physique
        illustrationInstance.delete(flush: true)
        redirect(controller: "annonce",action: "edit", id: annonceInstance.id)

    }
}
