package tpbeatiniortega

import grails.converters.JSON
import grails.converters.XML

import java.text.SimpleDateFormat

class ApiController {

    AnnonceService annonceService
    def pattern = "dd-MM-yyyy"

    def annonce() {

        switch(request.getMethod())
        {
            case "GET":
                if (!params.id)
                    return response.status = 400
                def annonceInstance = Annonce.get(params.id)
                if (!annonceInstance)
                    return response.status = 404
                response.withFormat {
                    json { render annonceInstance as JSON}
                    xml { render annonceInstance as XML }
                }
                break
            case "PUT":
                //println("dsqkjhksdfhksqdjlksqjlksdqjlksdqjsqdkhsdjkhsdkjsqdhkjsqdhjksdhsjdkqbcxbxccxns")
                println(request.getHeader("Accept"))
                if (!request.JSON.id)
                    return response.status = 400
                def annonceInstance = Annonce.get(request.JSON.id)
                annonceInstance.properties = request.JSON
                if (!annonceInstance)
                    return response.status = 404
                annonceInstance = annonceInstance.save flush:true
                response.withFormat {
                    json { render annonceInstance as JSON}
                    xml { render annonceInstance as XML }
                }

                break
            case "PATCH":
                break
            case "DELETE":
                println("Delete API")
                if (!params.id)
                    return response.status = 400
                def annonceInstance = Annonce.get(params.id)
                if (!annonceInstance)
                    return response.status = 404
                annonceInstance = annonceInstance.delete flush:true
                response.withFormat {
                    json { render annonceInstance as JSON}
                    xml { render annonceInstance as XML }
                }
                break
            default:
                return response.status = 405
                break
        }
        return response.status = 406
    }
    def annonces() {
        println "Reponse JSON : "+request.JSON
        switch(request.getMethod())
        {
            case "GET":
                def lesAnnonces  = Annonce.list(params)
                response.withFormat {
                    json { render lesAnnonces as JSON}
                    xml { render lesAnnonces as XML }
                }
                break
            case "POST":

                if(!request.JSON.title || !request.JSON.description || !request.JSON.validTill)
                    return response.status = 404
                def newAnnonce = new Annonce(
                        title: request.JSON.title,
                        description: request.JSON.description,
                        validTill: new SimpleDateFormat(pattern).parse(request.JSON.validTill),
                        dateCreated: new Date(),
                        state: Boolean.TRUE
                )
                //TODO rajouter userId dans params
                newAnnonce.author = User.get(1)
                annonceService.save(newAnnonce)


                response.withFormat {
                    json { render newAnnonce as JSON}
                    xml { render newAnnonce as XML }
                }
                break
            default:
                return response.status = 405
                break
        }
        return response.status = 406
    }
}